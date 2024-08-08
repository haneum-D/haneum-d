import os
import time
import string
import json
import difflib
import azure.cognitiveservices.speech as speechsdk

def pronunciation_assessment_from_file(audio_path, text_to_read):
    speech_key = os.environ["AZURE_API_KEY"]
    service_region = "koreacentral"

    speech_config = speechsdk.SpeechConfig(subscription=speech_key, region=service_region)
    audio_config = speechsdk.audio.AudioConfig(filename=audio_path)

    reference_text = text_to_read

    enable_miscue = True
    pronunciation_config = speechsdk.PronunciationAssessmentConfig(
        reference_text=reference_text,
        grading_system=speechsdk.PronunciationAssessmentGradingSystem.HundredMark,
        granularity=speechsdk.PronunciationAssessmentGranularity.Phoneme,
        enable_miscue=enable_miscue)

    speech_recognizer = speechsdk.SpeechRecognizer(speech_config=speech_config, language='ko-KR', audio_config=audio_config)
    pronunciation_config.apply_to(speech_recognizer)

    done = False
    recognized_words = []
    fluency_scores = []
    durations = []

    def stop_cb(evt: speechsdk.SessionEventArgs):
        #callback that signals to stop continuous recognition upon receiving an event `evt`
        #print('CLOSING on {}'.format(evt))
        nonlocal done
        done = True

    def recognized(evt: speechsdk.SpeechRecognitionEventArgs):
        #print('pronunciation assessment for: {}'.format(evt.result.text))
        pronunciation_result = speechsdk.PronunciationAssessmentResult(evt.result)
        """print('Accuracy score: {}, pronunciation score: {}, completeness score : {}, fluency score: {}'.format(
            pronunciation_result.accuracy_score, pronunciation_result.pronunciation_score,
            pronunciation_result.completeness_score, pronunciation_result.fluency_score
        ))"""
        nonlocal recognized_words, fluency_scores, durations
        recognized_words += pronunciation_result.words
        fluency_scores.append(pronunciation_result.fluency_score)
        json_result = evt.result.properties.get(speechsdk.PropertyId.SpeechServiceResponse_JsonResult)
        jo = json.loads(json_result)
        nb = jo['NBest'][0]
        durations.append(sum([int(w['Duration']) for w in nb['Words']]))

    # Connect callbacks to the events fired by the speech recognizer
    speech_recognizer.recognized.connect(recognized)
    speech_recognizer.session_started.connect(lambda evt: print('S: {}'.format(evt.session_id))) #Started
    speech_recognizer.session_stopped.connect(lambda evt: print('E: {}'.format(evt.session_id))) #End (Stop)
    speech_recognizer.canceled.connect(lambda evt: print('C {}'.format(evt.session_id))) #Cancled
    # stop continuous recognition on either session stopped or canceled events
    speech_recognizer.session_stopped.connect(stop_cb)
    speech_recognizer.canceled.connect(stop_cb)

    # Start continuous pronunciation assessment
    speech_recognizer.start_continuous_recognition()
    while not done:
        time.sleep(.5)

    speech_recognizer.stop_continuous_recognition()

    reference_words = [w.strip(string.punctuation) for w in reference_text.lower().split()]

    if enable_miscue:
        diff = difflib.SequenceMatcher(None, reference_words, [x.word.lower() for x in recognized_words])
        final_words = []
        for tag, i1, i2, j1, j2 in diff.get_opcodes():
            if tag in ['insert', 'replace']:
                for word in recognized_words[j1:j2]:
                    if word.error_type == 'None':
                        word._error_type = 'Insertion'
                    final_words.append(word)
            if tag in ['delete', 'replace']:
                for word_text in reference_words[i1:i2]:
                    word = speechsdk.PronunciationAssessmentWordResult({
                        'Word': word_text,
                        'PronunciationAssessment': {
                            'ErrorType': 'Omission',
                        }
                    })
                    final_words.append(word)
            if tag == 'equal':
                final_words += recognized_words[j1:j2]
    else:
        final_words = recognized_words

    # Calculate whole accuracy by averaging
    final_accuracy_scores = []
    for word in final_words:
        if word.error_type == 'Insertion':
            continue
        else:
            final_accuracy_scores.append(word.accuracy_score)
    accuracy_score = sum(final_accuracy_scores) / len(final_accuracy_scores)
    # Re-calculate fluency score
    fluency_score = sum([x * y for (x, y) in zip(fluency_scores, durations)]) / sum(durations)
    # Calculate whole completeness score
    completeness_score = len([w for w in recognized_words if w.error_type == "None"]) / len(reference_words) * 100
    completeness_score = completeness_score if completeness_score <= 100 else 100

    pron_score = accuracy_score * 0.4 + fluency_score * 0.3 + completeness_score * 0.3
    total_score_dict = {'pron_score':pron_score, 'accuracy_score':accuracy_score,
                  'completeness_score':completeness_score, 'fluency_score':fluency_score}
    words_score_list = []
    for idx, word in enumerate(final_words):
        temp_dict = {}
        temp_dict["word"] = str(word.word)
        temp_dict["score"] = int(word.accuracy_score)
        temp_dict["type"] = str(word.error_type)
        words_score_list.append(temp_dict)
    return {"status":"ok", "total_score":total_score_dict, "words_score":words_score_list}


if __name__ == '__main__':
    a = pronunciation_assessment_from_file("./sample1.wav","코가 막히고 목이 따끔거리고 기침이 나요")
