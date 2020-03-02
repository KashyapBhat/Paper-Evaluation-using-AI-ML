#!/usr/bin/env python
# coding: utf-8

# In[ ]:


# Get data from terminal or input
import sys
student_usn = sys.argv[1]


# In[ ]:


#connect to local server and accessing our database named aep
import pymysql
db = pymysql.connect("localhost","root","password","aep" )
cursor = db.cursor()
cursor.execute("SELECT * from answer where usn=%s",student_usn)
student_data = cursor.fetchall()


# In[ ]:


#asigning initial values as a student has 6 subjects
total_marks_in_a_subject = [0,0,0,0,0,0]
total_marks_of_student = 0


# In[ ]:


#get data from device storage and image handling libraries
import os,re
import cv2
try:
    from PIL import Image
except ImportError:
    import Image
import pytesseract


# In[ ]:


def getTextFromImageUsingOCR(file_to_ocr):
    
    # img = cv2.imread(file_to_ocr,0)
    # ret2,th2 = cv2.threshold(img,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
    # txt=pytesseract.image_to_string(th2)

    im = Image.open(os.path.join(file_to_ocr))
    txt=pytesseract.image_to_string(im)
    return txt


# In[ ]:


# fetch image locations and send to ocr engine
directory = os.path.join(r"C:\Users\hp\Desktop\PROJECT_AEIP\AEIP\STUDENTANSWER")
image_path='C:/xampp/htdocs/AEP/'
for n in range(len(student_data)):
    path=directory+"\\"+str(student_data[n][0])
    image_path+=str(student_data[n][0])
    try:  
        if os.path.isdir(path)==False:  
            os.mkdir(path)
    except OSError:  
        print ("Creation of the directory %s failed" % path)
    for j in range(1,len(student_data[n])):
        if len(student_data[n][j])!=0:
            a=re.split(r' *[\,\?!][\""\)\]]* *',student_data[n][j])
            for i in range(len(a)-1):
                if (os.path.isfile(path+"\\"+a[i].split('/' )[1].split('.')[0][0]+".txt")):
                    os.remove(path+"\\"+a[i].split('/' )[1].split('.')[0][0]+".txt")
            for i in range(len(a)-1):
                txt=getTextFromImageUsingOCR(image_path+"\\"+str(a[i].split('/' )[1]))
                m=a[i].split('/' )[1].split('.')[0][0]
                fname=path+'\\'+m+".txt"
                if (os.path.isfile(fname)):
                    with open(fname,'a') as f1: f1.write(str(txt))
                else:
                    with open(fname,'w') as f1: f1.write(str(txt)) 
    directory = os.path.join(r"C:\Users\hp\Desktop\PROJECT_AEIP\AEIP\STUDENTANSWER")
    image_path='C:/xampp/htdocs/AEP/'


# In[ ]:


def ansLen(num_words,ans_length):
    if num_words > 250:
        marks=10
    elif num_words >230 and num_words <250:
        marks=9
    elif num_words > 200 and num_words <230:
        marks=8
    elif num_words >180 and num_words < 200:
        marks=7
    elif num_words > 160 and num_words < 180:
        marks=6
    elif num_words > 140 and num_words< 160:
        marks=5
    elif num_words >110 and num_words <140:
        marks=4
    elif num_words > 80 and num_words<110:
        marks=3
    elif num_words>40 and num_words<80:
        marks=2
    elif num_words >3 and num_words<40:
        marks=1
    else:
        marks=0
    if ans_length >=15:
        m=5
    elif ans_length>=13 and ans_length<15:
        m=4
    elif ans_length>=10 and ans_length<13:
        m=3
    elif ans_length>=5 and ans_length<10:
        m=2
    elif ans_length>1 and ans_length<5:
        m=1
    else:
        m=0
    total_marks_of_student=(marks+m)/2
    return int(total_marks_of_student)


# In[ ]:


def gramarCheck(Sans1,num_words,ans_length):
    import requests
    req = requests.get("https://api.textgears.com/check.php?text=" + Sans1+ "&key=JmcxHCCPZ7jfXLF6")
    no_of_errors = len(req.json()['errors'])
    count_spelling=0
    count_grammar=0
    for item in req.json()['errors']:
            if item['type'] == 'spelling':
                count_spelling+=1
            if item['type'] == 'grammar':
                count_grammar+=1
    error_rate1=count_grammar/ans_length
    error_rate2=count_spelling/num_words
    import math
    percentage_of_error=int(math.sqrt(error_rate1)+math.sqrt(error_rate2)*10)
    return percentage_of_error  


# In[ ]:


def preprocessTheSentence(stmt1):
    stmt1=stmt1.lower()
    #step1:Removing punctuations
    import string
    remove=dict.fromkeys(map(ord,'\n'+string.punctuation)," ")
    PSA1=stmt1.translate(remove)
    #step2:Removing stop words
    from nltk.corpus import stopwords
    from nltk.tokenize import word_tokenize 
    stop_words = set(stopwords.words('english'))
    word_tokens = word_tokenize(PSA1) 
    PSA2 = [w for w in word_tokens if not w in stop_words]
    #step3:Removing prepositions
    import nltk
    a=list()
    b=list()
    PSA3=" "
    for i in range(len(PSA2)):
        a=list(nltk.pos_tag([PSA2[i]]))
        if a[0][1]!='IN':
            b.append(PSA2[i])
        PSA3=" ".join(b)
    return PSA3


# In[ ]:


def matchAnswersWithKeyAnswer(Sans1,Kans1):
    from nltk.tokenize import sent_tokenize
    stmts=sent_tokenize(Sans1)
    answer=""
    for i in range(len(stmts)):
        answer+=preprocessTheSentence(stmts[i])+'\n'
    stmts=sent_tokenize(Kans1)
    KEY=""
    for i in range(len(stmts)):
        KEY+=preprocessTheSentence(stmts[i])+'\n'
    def synkey(w):
        a3=list(set(word_tokenize(answer)))
        import nltk 
        from nltk.corpus import wordnet 
        synonyms = set()
        for syn in wordnet.synsets(w): 
            for l in syn.lemmas(): 
                synonyms.add(l.name()) 
        synonyms=list(synonyms)
        for i in range(len(synonyms)):
            if synonyms[i] in a3:
                return True
        return False
    from nltk.tokenize import word_tokenize
    a1=KEY.split('\n')
    a1.remove(a1[-1])
    count1=len(a1)
    for i in range(len(a1)):
        str2find=a1[i]
        m=answer.find(str2find)
        if m==-1:
            count1-=1
    count1=(count1/len(a1))*100
    a2=list(set(word_tokenize(KEY)))
    a3=list(set(word_tokenize(answer)))
    count2=0
    for i in range(len(a2)):
        if a2[i] in a3:
            count2+=1
        else:
            cndtn=synkey(a2[i])
            if cndtn:
                count2+=1
    count2=(count2/len(a2))*100     
    if count1>=count2:
        keyMatchRatio=count1
    else:
        keyMatchRatio=count2
    return keyMatchRatio 


# In[ ]:



def evaluateCprogram(quesno2,student_usn,filename):
    listc=[0,0,0]
    
    for xyz in range(1,4):
        
        import os
        directory1 = os.path.join(r"C:\Users\hp\Desktop\PROJECT_AEIP\AEIP\STUDENTANSWER")
        path1=directory1+"\\"+str(student_usn)
        f1=path1+"\\"+str(filename)
        image_path='C:/xampp/htdocs/AEP/'
        image_path+="submit"+str(quesno2)+"\\"+"testcase"+str(xyz)+"_1.txt"
        
        ff1=open('a1.c','w')
        ff1.truncate(0)
        # new code
        import os.path
        from os import path
        # print(path.exists(f1))
        if(path.exists(f1)):
            with open(f1) as myf:
                for num,line in enumerate(myf,1):
                    ff1=open('a1.c','a+')
                    ff1.write(line)
            ff1=open('a1.c','a+')
            ff1.write("\n")
            
            search=['printf','scanf','gets','puts','getc','putc','getchar','putchar']
            char_string=""
            f=open('test11.txt','w')
            f.truncate(0)
            with open("a1.c") as myFile:
                for num,line in enumerate(myFile,1):
                    if "printf" in line or "scanf" in line or "puts" in line or "gets" in line or "putch" in line or "getch" in line or "putchar" in line or "getchar" in line:
                        f=open('test11.txt','a+')
                        f.write(line)
                    if "char" in line.split(" "):
                        char_string+=line        

            printf_line_number=list()
            with open('test11.txt') as f:
                for num,line in enumerate(f,1):
                    if "printf" in line or "puts" in line or "putch" in line or "putchar" in line: 
                        printf_line_number.append(num) 

            variable_list1=list()
            line_numbers_list1=list()
            variable_line1=list()
            import re
            lookup="scanf"
            lookup1="gets"
            lookup2="getc"
            lookup3="getchar"
            str1=""
            def find_variable(n,num):
                var=""
                for i in range(n,len(str1)):
                    if str1[i].isalnum():
                        var+=str1[i]
                    else:
                        variable_list1.append(var)
                        variable_line1.append(num)
                        return i
            with open('a1.c') as myFile:
                for num,line in enumerate(myFile,1):
                    if lookup in line:
                        line_numbers_list1.append(num)
                        str1=line
                        n=str1.find("&")+1
                        i=find_variable(n,num)
                        while str1[i]!=")":
                            if str1[i]=="," and str1[i+1]=="&":
                                n=i+2
                                i=find_variable(n,num)
                    elif lookup1 in line:
                        line_numbers_list1.append(num)
                        str1=line
                        n=str1.find("(")+1
                        n1=str1.find(")")
                        variable_list1.append(str1[n:n1])       
                        variable_line1.append(num)

                    elif lookup2 in line or lookup3 in line:
                        line_numbers_list1.append(num)
                        str1=line
                        b=str1.split("=")[0]
                        if "char" in b.split(" "):
                            variable_list1.append(b.split(" ")[1])
                        else:
                            variable_list1.append(b)       
                        variable_line1.append(num)

            y=list()
            a=["\n","\t",'']
            with open(image_path) as f1:
                for num,line in enumerate(f1,1):
                    if num not in printf_line_number:
                        x=line.split(",")
                        for i in range(len(x)):
                            if x[i] not in a:
                                y.append(x[i])

            variable_list1 = [x.strip(' ') for x in variable_list1]
            print(variable_list1)
            print(printf_line_number)
            
            m=variable_line1[0]
            lineM=""
            double_quotes="\'"
            for i in range(len(y)-1):
                a=variable_list1[i]
                lineM+=a
                lineM+="="
                b=y[i]
                if a in char_string:
                    lineM+=double_quotes+b+double_quotes
                else:
                    lineM+=b
                lineM+=","
            a=variable_list1[-1]
            b=y[-1]
            lineM+=a
            lineM+="="
            lineM+=b
            
            lineM
            
            print(line_numbers_list1)
            pgm1=""
            str1="printf(\"\\n\");"
            i=0
            f=open('z1.c','w')
            f.truncate(0)
            with open('a1.c') as myFile:
                for num,line in enumerate(myFile,1):   
                    if num not in line_numbers_list1:
                        pgm1=line 
                        f=open("z1.c","a+") 
                        f.write(pgm1)
                    elif m==num:
                        pgm1=lineM+";\n"
                        f=open("z1.c","a+") 
                        f.write(pgm1)
                        
            import subprocess
            subprocess.getoutput(["g++","z1.c"])
            
            f=open('tmp.txt','w')
            f.truncate(0)
            f=open('tmp.txt','w')
            tmp1=subprocess.getoutput("a.exe")
            f.write(tmp1)

            f1=open('tmp2.txt','w')
            f1.truncate(0)
            with open(image_path) as f1:
                for num,line in enumerate(f1,1):
                    if num in printf_line_number:
                        f1=open("tmp2.txt","a+")
                        f1.write(line)
            f1=open("tmp2.txt","a+")
            f1.write("\n")

            f=open('tmp2.txt','r')
            for i in f:
                print(i)
                
            num_lines = 0
            with open("tmp.txt", 'r') as f:
                for line in f:
                    num_lines += 1

            with open('tmp.txt') as f1:
                with open('tmp2.txt') as f2:

                    f1_line = f1.readline()
                    f2_line = f2.readline()

                    # Initialize counter for line number
                    line_no = 1
                    count=0

                    # Loop if either file1 or file2 has not reached EOF
                    while f1_line != '' or f2_line != '':

                        # Strip the leading whitespaces
                        f1_line = f1_line.rstrip()
                        f2_line = f2_line.rstrip()

                        # Compare the lines from both file
                        if f1_line == f2_line:
                            count += 1

                        #Read the next line from the file
                        f1_line = f1.readline()
                        f2_line = f2.readline()


                        #Increment line counter
                        line_no += 1
            if(count==num_lines):
                listc[xyz-1]=10
            
            else:
                import math
                marksss=math.ceil((count/num_lines)*5)
                listc[xyz-1]=marksss
            
    program_certeria=(listc[0]+listc[1]+listc[2])/3
        
    return program_certeria


# In[ ]:


# fuzzy logic to get the similarities
import re, math
from collections import Counter
import fuzzywuzzy.fuzz


# In[ ]:


def get_cosine(vec1, vec2):
    intersection = set(vec1.keys()) & set(vec2.keys())
    numerator = sum([vec1[x] * vec2[x] for x in intersection])

    sum1 = sum([vec1[x] ** 2 for x in vec1.keys()])
    sum2 = sum([vec2[x] ** 2 for x in vec2.keys()])
    denominator = math.sqrt(sum1) * math.sqrt(sum2)

    if not denominator:
        return 0.0
    else:
        return float(numerator) / denominator


# In[ ]:


def text_to_vector(text):
    words = re.compile(r'\w+').findall(text)
    return Counter(words)


# In[ ]:


def givKeywordsValue(text1, text2):
    vector1 = text_to_vector(text1)
    vector2 = text_to_vector(text2)
    cosine = round(get_cosine(vector1, vector2),2)*100
    return cosine


# In[ ]:


def textBookMatch(Sans1,Tans1):
    cosine=givKeywordsValue(Sans1,Tans1)
    # kval = 0
    # if cosine > 90:
    #     kval = 6
    # elif cosine > 80:
    #     kval = 5
    # elif cosine > 60:
    #     kval = 4
    # elif cosine > 40:
    #     kval = 3
    # elif cosine > 20:
    #     kval = 2
    # else:
    #     kval = 1
    return cosine   


# In[ ]:


def marks_for_each_question(usn1,quesno,answer_length_check_creteria,grammer_check_creteria,key_word_match_creteria,text_book_match_creteria):
    db1 = pymysql.connect("localhost","root","mite","aep" )
    cursor1 = db1.cursor()
    cursor1.execute("SELECT totalmarks from question where qno=%s",quesno)
    data1 = cursor1.fetchall()
    
    fetch_marks=data1[0][0]
    
    ans_length_weight=2.0
    if answer_length_check_creteria>6 and answer_length_check_creteria<=7.5:
        ans_length_weight=2.0
    elif answer_length_check_creteria>5 and answer_length_check_creteria<=6:
        ans_length_weight=(80*ans_length_weight)/100
    elif answer_length_check_creteria>4 and answer_length_check_creteria<=5:
        ans_length_weight=(70*ans_length_weight)/100
    elif answer_length_check_creteria>3 and answer_length_check_creteria<=4:
        ans_length_weight=(50*ans_length_weight)/100
    elif answer_length_check_creteria>2 and answer_length_check_creteria<=3:
        ans_length_weight=(30*ans_length_weight)/100
    elif answer_length_check_creteria>0 and answer_length_check_creteria<=2:
        ans_length_weight=(5*ans_length_weight)/100
    
    answer_length_check_total=(fetch_marks*ans_length_weight)/10
    
    grammer_weight=1.0
    if grammer_check_creteria>=1 and grammer_check_creteria<=3:
        grammer_weight=1
    elif grammer_check_creteria>=4 and grammer_check_creteria<=6:
        grammer_weight=(70*grammer_weight)/100
    elif grammer_check_creteria>=7 and grammer_check_creteria<=10:
        grammer_weight=(40*grammer_weight)/100
    elif grammer_check_creteria>10:
        grammer_weight=0
    
    grammer_check_total=(fetch_marks*grammer_weight)/10
    
    keyword_match_weight=4
    if key_word_match_creteria<10:
        keyword_match_weight=0.4
    elif key_word_match_creteria>=10 and key_word_match_creteria<=19:
        keyword_match_weight=(20*keyword_match_weight)/100
    elif key_word_match_creteria>=20 and key_word_match_creteria<=29:
        keyword_match_weight=(30*keyword_match_weight)/100
    elif key_word_match_creteria>=30 and key_word_match_creteria<=39:
        keyword_match_weight=(40*keyword_match_weight)/100
    elif key_word_match_creteria>=40 and key_word_match_creteria<=49:
        keyword_match_weight=(50*keyword_match_weight)/100
    elif key_word_match_creteria>=50 and key_word_match_creteria<=59:
        keyword_match_weight=(60*keyword_match_weight)/100
    elif key_word_match_creteria>=60 and key_word_match_creteria<=69:
        keyword_match_weight=(70*keyword_match_weight)/100    
    elif key_word_match_creteria>=70 and key_word_match_creteria<=79:
        keyword_match_weight=(80*keyword_match_weight)/100
    elif key_word_match_creteria>=80 and key_word_match_creteria<=89:
        keyword_match_weight=(90*keyword_match_weight)/100
    elif key_word_match_creteria>=90:
        keyword_match_weight=4
        
    key_word_match_total=(fetch_marks*keyword_match_weight)/10  
    
    textbook_match_weight=3
    
    if text_book_match_creteria<40:
        textbook_match_weight=(10*textbook_match_weight)/100
    elif text_book_match_creteria>=40 and text_book_match_creteria<60:
        textbook_match_weight=(30*textbook_match_weight)/100
    elif text_book_match_creteria>=60 and text_book_match_creteria<80:
        textbook_match_weight=(50*textbook_match_weight)/100
    elif text_book_match_creteria>=80:
        textbook_match_weight=3.0
        
    text_book_match_total=(fetch_marks*textbook_match_weight)/10  
    
    import math
    t=round(answer_length_check_total+grammer_check_total+key_word_match_total+text_book_match_total)
    # t=answer_length_check_total+grammer_check_total+key_word_match_total+text_book_match_total
    print(answer_length_check_total)
    print(grammer_check_total)
    print(key_word_match_total)
    print(text_book_match_total)

    indexx=quesno-1
    total_marks_in_a_subject[indexx]=t
      
    return 1


# In[ ]:


directory1 = os.path.join(r"C:\Users\hp\Desktop\PROJECT_AEIP\AEIP\STUDENTANSWER")
directory2 =os.path.join(r"C:\Users\hp\Desktop\PROJECT_AEIP\AEIP\KEYANSWER")
directory3 = os.path.join(r"C:\Users\hp\Desktop\PROJECT_AEIP\AEIP\OUTPUT")
path1=directory1+"\\"+str(student_usn)
list_ans=['a.txt','b.txt','c.txt','d.txt','e.txt','f.txt']
if(os.path.isdir(path1) ==True):
    for i in range(len(list_ans)):
        
        f1=path1+"\\"+str(list_ans[i])
        
        quesno2=int(i+1)
        db2 = pymysql.connect("localhost","root","mite","aep" )
        cursor2 = db2.cursor()
        cursor2.execute("SELECT * from question where qno=%s",quesno2)
        data2 = cursor2.fetchall()
        print(data2)
        if(data2):
            if(data2[0][8]==1):
                
                if(os.path.isfile(f1)):
                    f2=open(f1,'r')
                    Sans=""
                    for each in f2:
                        Sans=Sans+each
                    path2=directory2+"\\"+"key"+str(i+1)+".txt"
                    if(os.path.isfile(path2)):
                        f3=open(path2,'r')
                        Kans=""
                        for each in f3:
                            Kans=Kans+each
                    path3=directory3+"\\"+"textans"+str(i+1)+".txt"
                    if(os.path.isfile(path3)):
                        f4=open(path3,'r')
                        Tans=""
                        for each in f4:
                            Tans=Tans+each
                    f1=path1+"\\"+str(list_ans[i])
                    if(os.path.isfile(f1)):
                        num_words=0
                        with open(f1, 'r') as f:
                            for line in f:
                                words = line.split()
                                num_words += len(words)
                    f1=path1+"\\"+str(list_ans[i])
                    file_ans = open(f1, 'r') 
                    text=' '.join(file_ans.readlines())
                    import re
                    sentences=re.split(r' *[\.\?!][\""\)\]]* *',text)
                    ans_length=len(sentences)-1
                    if ans_length<=0:
                        ans_length=1


                    answer_length_check_creteria=ansLen(num_words,ans_length)
                    answer_length_check_creteria=float(answer_length_check_creteria)
                    grammer_check_creteria=gramarCheck(Sans,num_words,ans_length)
                    grammer_check_creteria=float(grammer_check_creteria)
                    key_word_match_creteria=matchAnswersWithKeyAnswer(Sans,Kans)
                    key_word_match_creteria=float(key_word_match_creteria)
                    text_book_match_creteria=textBookMatch(Sans,Tans)
                    text_book_match_creteria=float(text_book_match_creteria)
                    marks_for_each_question(student_usn,int(i+1),answer_length_check_creteria,grammer_check_creteria,key_word_match_creteria,text_book_match_creteria)
                    
            elif(data2[0][9]==1):
                
                program_certeria=evaluateCprogram(quesno2,student_usn,list_ans[i])
                program_certeria=float(program_certeria)
                
                db4 = pymysql.connect("localhost","root","mite","aep" )
                cursor4 = db4.cursor()
                cursor4.execute("SELECT totalmarks from question where qno=%s",quesno2)
                data4 = cursor4.fetchall()

                fetch_marks=data4[0][0]
                
                program_mark=(fetch_marks*program_certeria)/10  
                total_marks_in_a_subject[quesno2-1]=program_mark
            
    if(total_marks_in_a_subject[0]>total_marks_in_a_subject[1]):
        total_marks_of_student=total_marks_of_student+total_marks_in_a_subject[0]
    else:
        total_marks_of_student=total_marks_of_student+total_marks_in_a_subject[1]
        
        
    if(total_marks_in_a_subject[2]>total_marks_in_a_subject[3]):
        total_marks_of_student=total_marks_of_student+total_marks_in_a_subject[2]
    else:
        total_marks_of_student=total_marks_of_student+total_marks_in_a_subject[3] 
        
        
    if(total_marks_in_a_subject[4]>total_marks_in_a_subject[5]):
        total_marks_of_student=total_marks_of_student+total_marks_in_a_subject[4]
    else:
        total_marks_of_student=total_marks_of_student+total_marks_in_a_subject[5]
    
    import math
    total_marks_of_student=round(total_marks_of_student)


# In[ ]:


#store all these values back to db
   db = pymysql.connect("localhost","root","mite","aep" )
   cursor = db.cursor()
   
   sql = "UPDATE answer SET m1 = %s, m2=%s, m3=%s, m4=%s, m5=%s, m6=%s, total=%s WHERE usn = %s"
   val = (total_marks_in_a_subject[0],total_marks_in_a_subject[1],total_marks_in_a_subject[2],total_marks_in_a_subject[3],total_marks_in_a_subject[4],total_marks_in_a_subject[5],total_marks_of_student,student_usn)
   cursor.execute(sql, val)
   db.commit()

   db5 = pymysql.connect("localhost","root","mite","aep" )
   cursor5 = db5.cursor()
   cursor5.execute("SELECT * from subject_details")
   data5 = cursor5.fetchall()
   subject_code=data5[0][0]
   internal=data5[0][3]

   db6 = pymysql.connect("localhost","root","mite","aep" )
   cursor6 = db6.cursor()
   
   sql6 = "INSERT INTO `student_history` (`usn`,`internal`,`subject_code`,`total`) VALUES (%s,%s,%s,%s)"
   val6 = (student_usn,internal,subject_code,total_marks_of_student)
   cursor6.execute(sql6, val6)
   db6.commit()

