'''
Created on Jul 27, 2012

@author: bowu
'''
import re
import string
import math
from FeatureFactory import *
import pickle
'''template function section'''
def indexOf(str, lregx, rregx, cnt=0):
        '''find the position'''
        pos = -1
        if lregx == "^":
           if cnt != 1 and cnt != -1:
               return None
           pos = 0
           return pos
        if rregx == "$":
           if cnt != 1 and cnt != -1:
               return None
           pos = len(str)
           return pos
        patternstr = "(" + lregx + ")" + rregx
        pattern = re.compile(patternstr)
        tpos = 0
        poslist = []
        while True:
            m = pattern.search(str, tpos)
            if m == None:
                break
            if len(m.groups()) <2:
                tpos = m.start() + 1
            else:
                tpos = m.start() + len(m.group(2))
            poslist.append(m.start()+len(m.group(1)))
        index = 0;
        if cnt > 0:
            index = cnt -1
        else:
            index = len(poslist)+cnt
        if len(poslist) == 0 or index >= len(poslist) or index<0:
            return None
        return poslist[index]
def loop(value, stript):
    res = "";
    cnt = 1;
    while True:
        tmpstript = stript
        if tmpstript.find("counter") == -1:
            break
        tmpstript = tmpstript.replace("counter", str(cnt))
        s = eval(tmpstript)
        if s.find("<_FATAL_ERROR_>") != -1:
            break
        res += s
        cnt += 1
    return res
def substr(str, p1, p2):
    '''get substring'''
    if p1 == None or p2 == None:
        return "<_FATAL_ERROR_>"
    res = str[p1:p2]
    if res != None:
        return res
    else:
        print '''cannot get substring using''' + str + '(' + p1 + ',' + p2 + ')'
        return "<_FATAL_ERROR_>"
def foreach(elems, exps):
    '''for each '''
    for i in range(len(elems)):
        value = elems[i]
        elems[i] = eval(exps)
    return elems
def switch(tuplelist):
    for (condi, expre) in tuplelist:
        c = str(condi)
        if c == "True":
            return expre # already evaluated directly return
def getClass(setting, value):
    setting = setting.decode("string-escape")
    #print setting
    classifier = pickle.loads(setting)
    feature = FeatureFactory()
    feature.createFeature(value, "")
    dict = {};
    dict['attributes'] = {}
    attributes = []
    line = feature.datatable[0]
    for i in range(len(line)):
        dict['attributes'][str(i)] = line[i]
        attributes.append(str(i))
    res = classifier.predict(dict)
    r = max(res.iterkeys(), key=lambda k: res[k])     
    return r        
        
