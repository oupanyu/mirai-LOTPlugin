#coding=utf-8
import json,wget,sys,http.client,urllib,os

if os.path.exists("downloads") != True:
	os.makedirs("downloads")
os.chdir("downloads")
while(1):
	keyWord = input("请输入歌曲名，请尽量完整: ")
	key = urllib.parse.quote(keyWord)
	connA = http.client.HTTPConnection('mobilecdn.kugou.com')  #接口域名
	paramsA = urllib.parse.urlencode({})
	headersA = {'Content-type':'application/x-www-form-urlencoded'}
	connA.request('POST','/api/v3/search/song?keyword=%s'%key,paramsA,headersA)
	resA = connA.getresponse()
	dataA = resA.read()
	a = json.loads(dataA.decode('utf-8'))
	b = a["data"]
	info = b["info"]
	songname = info[0]["filename"]
	hash = info[0]["hash"]
	connB = http.client.HTTPConnection('m.kugou.com')
	paramsB = urllib.parse.urlencode({})
	headersB = {'Content-type':'application/x-www-form-urlencoded'}
	connB.request('POST','/app/i/getSongInfo.php?cmd=playInfo&hash=%s'%hash,paramsB,headersB)
	resB = connB.getresponse()
	dataB = resB.read()
	jsonEvent = json.loads(dataB.decode('utf-8'))
	URL = jsonEvent["url"]
	if URL == '':
		print("\n酷狗说宁没会员下不了呐")
		sys.exit()
	print("即将下载:"+songname)
	print("哈希值为:%s\n文件名可能为%s.mp3\n请注意核对"%(hash,hash))
	wget.download(URL)

