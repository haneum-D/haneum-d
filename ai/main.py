import os
import traceback
import tempfile
from fastapi import FastAPI, UploadFile, File, Form, HTTPException

from audio_convert import *
from clova_speech import *
from pronounce_assess import *
from temp_prompts import *

"""TTS Connection Legacy
from datetime import datetime
from fastapi.responses import FileResponse
from starlette.background import BackgroundTask
from custom_llm_clova import *"""

app = FastAPI()

if __name__ == '__main__':
    import uvicorn
    print("\nMain RUN OK\n")
    uvicorn.run("main:app",reload=True)



@app.post("/lev1_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: str=Form(...)):
    try:
        content = await audio_file.read()
        with tempfile.NamedTemporaryFile(delete=False, suffix='.mp3',mode='wb') as aud:
            aud.write(content)
        speechfile = wav_converter(aud.name)
        idx = content_idx.split(",")
        if(idx[2] is not None):
            if(idx[1]=='0'):
                result_dict = short_pro_assessment(speechfile, content_texts['set'+idx[0]][int(idx[1])][int(idx[2])])
            else:
                result_dict = pronunciation_assessment_from_file(speechfile, content_texts['set'+idx[0]][int(idx[1])][int(idx[2])])
        else:
            result_dict = "error occured"
        if(aud.name is not None):
            os.remove(aud.name)
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(text)
        print(traceback.format_exc())
        raise HTTPException(status_code=420, detail = text)
    return result_dict

@app.post("/lev2_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: str=Form(...)):
    try:
        content = await audio_file.read()
        with tempfile.NamedTemporaryFile(delete=False, suffix='.mp3',mode='wb') as aud:
            aud.write(content)
        speechfile = wav_converter(aud.name)
        idx = content_idx.split(",")
        if(idx[1] is not None):
            stt_result = clova_stt(speechfile)
            if(content_idx in number_issue_idx):
                keyword_2nd_check = False
                for keyword in keyword_texts['set'+idx[0]][int(idx[1])][0]:
                    print(keyword)
                    if(keyword not in stt_result):
                        keyword_2nd_check = True
                        break
                if(keyword_2nd_check):
                    for keyword in keyword_texts['set'+idx[0]][int(idx[1])][1]:
                        print(keyword)
                        if(keyword not in stt_result):
                            result_dict = {}
                            result_dict["status"] = "ok"
                            result_dict["total_score"] = 0
                            result_dict["keyword"] = "notin"
                            return result_dict
                result_dict = pronunciation_assessment_from_file(speechfile, stt_result)
                result_dict["stt_result"] = stt_result
                result_dict["keyword"] = "in"
            else:
                for keyword in keyword_texts['set'+idx[0]][int(idx[1])]:
                    print(keyword)
                    if(keyword not in stt_result):
                        result_dict = {}
                        result_dict["status"] = "ok"
                        result_dict["total_score"] = 0
                        result_dict["keyword"] = "notin"
                        return result_dict
                result_dict = pronunciation_assessment_from_file(speechfile, stt_result)
                result_dict["stt_result"] = stt_result
                result_dict["keyword"] = "in"
        else:
            result_dict = "error occured"
        if(aud.name is not None):
            os.remove(aud.name)
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(text)
        print(traceback.format_exc())
        raise HTTPException(status_code=420, detail = text)
    return result_dict
