from pydub import AudioSegment
from pydub.utils import mediainfo

def wav_converter(speechfile):
    info = mediainfo(speechfile)
    if(info['sample_rate'] != 16000 or speechfile.split('.')[1] != "wav"):
        sound = AudioSegment.from_file(speechfile)
        sound = sound.set_frame_rate(16000)
        speechfile = speechfile.split('.')[0]+".wav"
        sound.export(speechfile, format="wav", bitrate="128k")
    return speechfile
