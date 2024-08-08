import os
import sys
import json
import requests


def clova_stt(audio_path):
    lang = "Kor" # 언어 코드 ( Kor, Jpn, Eng, Chn )
    data = open(audio_path, 'rb')

    url = "	https://clovaspeech-gw.ncloud.com/recog/v1/stt?lang="+ lang
    headers = {
        "X-CLOVASPEECH-API-KEY": os.environ["CLOVA_API_KEY"],
        "Content-Type": "application/octet-stream"
    }

    response = requests.post(url,  data=data, headers=headers)
    rescode = response.status_code
    res = json.loads(response.text)

    if(rescode == 200):
        return res["text"]
    else:
        return "error"

if __name__ == '__main__':
    testpath = './sample1.wav'
    res= clova_stt(testpath)
    print(res)
