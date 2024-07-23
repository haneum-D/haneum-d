import os
import traceback
import tempfile
from datetime import datetime
from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from fastapi.responses import FileResponse
from starlette.background import BackgroundTask

from clova_stt_speech import *
from custom_llm_clova import *
from pro_assess import *

app = FastAPI()

if __name__ == '__main__':
    import uvicorn
    print("\nMain RUN OK\n")
    uvicorn.run("main:app",reload=True)



@app.post("/lev1_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: int=Form(...)):
    try:
        content = await audio_file.read()
        with tempfile.NamedTemporaryFile(delete=False, suffix='.mp3',mode='wb') as aud:
            aud.write(content)
        #speechfile = wav_converter(aud.name)
        result_dict = pronunciation_assessment_from_file(aud.name, "dummy")
        if(aud.name is not None):
            os.remove(aud.name)
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(text)
        print(traceback.format_exc())
        raise HTTPException(status_code=420, detail = text)
    return result_dict


@app.post("/chat/{roleplay}")
async def post_chat_role_play(roleplay: str, history_msg: str=Form(...), audio_file: UploadFile=File(...)):
    try:
        content = await audio_file.read()
        with tempfile.NamedTemporaryFile(delete=False, suffix='.mp3',mode='wb') as aud:
            aud.write(content)
        stt_result = clova_stt(aud.name,True)
        
        #pronounce assessment
        #result_dict = pronunciation_assessment_from_file(speechfile, stt_result)
        #result_dict["stt_result"] = stt_result

        if(aud.name is not None):
            os.remove(aud.name)
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(traceback.format_exc())
        print(text)
        raise HTTPException(status_code=420, detail = text)
    response = llm.invoke(stt_result)
    result_dict = {}
    result_dict['stt_result'] = str(stt_result)
    result_dict['chat_response'] = str(response)
    return result_dict
    


def cleanup(temp_file):
    os.remove(temp_file)

