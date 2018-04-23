This is a ReadMe file which contains the instructions for running the following JAVA files on HADOOP.

The files are:
1. DocWordCount.java
2. TermFrequency.java
3. TFIDF.java
4. Search.java

							----- All the commands are to be provided in the terminal. -----

Basic instructions for all files:

Set the Hadoop class path using the below commands.
1. export HADOOP_CLASSPATH=$(hadoop classpath)  - This will export your hadoop path in HADOOP_CLASSPATH.
2. echo $HADOOP_CLASSPATH                       - This will print your hadoop class path on the terminal.

							----- Instructions for DocWordCount.java file  ----- 

1. Compile your .java file using the following command:

	javac -classpath {$HADOOP_CLASSPATH} -d '/home/cloudera/Desktop/HW2/DocWordCount' '/home/cloudera/Desktop/HW2/DocWordCount.java'  

In the above command the first path after -d is the folder in which the .class file for the same will be created after the compilation, the second path is of the .java file which needs to be compiled.

2. Once the .class file is created for the file, we need to build a jar file for the same which will be containing the .class file, to create the .jar file below is the command:

	jar -cvf DocWordCount.jar -C '/home/cloudera/Desktop/HW2/DocWordCount/' .

Make sure that you are into the folder in which you want to create the jar file (for me its the DocWordCount folder), after the above command is executed the .jar file will be created for us.

3. Once the jar file is create we need to run this jar file on Hadoop and get the output of our java file. For running the jar file use the below command:
 
	hadoop jar '/home/cloudera/Desktop/HW2/DocWordCount/DocWordCount.jar' DocWordCount /user/cloudera/HW2/DocWordCount/input /user/cloudera/HW2/DocWordCount/output

For the above command the path after the 'jar' is the path of the .jar file for the corresponding class, then comes the class name for the same .java file then is the input directory which will give the input to the Mapper and after that is the ouput directory which will contain the outpur of the MapReduce job.

4. Once the .jar file is run using the above command the output will be genrated in the output directory. To view the output you can give the below command:
	
	$ hadoop dfs -cat /user/cloudera/HW2/DocWordCount/output1/p*

In the above command the last argument is the path for the output directory. Since the output file which is generated has the name part-r-00000, hence p* is used to get the output of our .java file. This will display the output on the terminal.

5. Now to save the output there are two options as described below:

	a. Go to the output directory in your browser and download the file.
	b. In the terminal you can give the following command: hadoop fs -getmerge /user/cloudera/HW2/DocWordCount/output1/p* DocWordCount.out 
	 This output will create teh output file in the same folder as the user is currently in so, its better to be in the directory in which you want your output file to be saved.


							----- Instructions for TermFrequency.java file  ----- 

1. Compile your .java file using the following command:

	javac -classpath {$HADOOP_CLASSPATH} -d '/home/cloudera/Desktop/HW2/TermFrequency/' '/home/cloudera/Desktop/HW2/TermFrequency.java'  

In the above command the first path after -d is the folder in which the .class file for the same will be created after the compilation, the second path is of the .java file which needs to be compiled.

2. Once the .class file is created for the file, we need to build a jar file for the same which will be containing the .class file, to create the .jar file below is the command:

	jar -cvf TermFrequency.jar -C '/home/cloudera/Desktop/HW2/TermFrequency/' . 

Make sure that you are into the folder in which you want to create the jar file (for me its the DocWordCount folder), after the above command is executed the .jar file will be created for us.

3. Once the jar file is create we need to run this jar file on Hadoop and get the output of our java file. For running the jar file use the below command:
 
	hadoop jar '/home/cloudera/Desktop/HW2/TermFrequency/TermFrequency.jar' TermFrequency /user/cloudera/HW2/DocWordCount/input /user/cloudera/HW2/TermFrequency/output1

For the above command the path after the 'jar' is the path of the .jar file for the corresponding class, then comes the class name for the same .java file then is the input directory which will give the input to the Mapper and after that is the ouput directory which will contain the outpur of the MapReduce job.

4. Once the .jar file is run using the above command the output will be genrated in the output directory. To view the output you can give the below command:
	
	$ hadoop dfs -cat /user/cloudera/HW2/TermFrequency/output1/p*

In the above command the last argument is the path for the output directory. Since the output file which is generated has the name part-r-00000, hence p* is used to get the output of our .java file. This will display the output on the terminal.

5. Now to save the output there are two options as described below:

	a. Go to the output directory in your browser and download the file.
	b. In the terminal you can give the following command: hadoop fs -getmerge /user/cloudera/HW2/TermFrequency/output1/p* TermFrequency.out 
	 This output will create teh output file in the same folder as the user is currently in so, its better to be in the directory in which you want your output file to be saved.

							----- Instructions for TFIDF.java file  ----- 
1. Compile your .java file using the following command:

	 javac -classpath {$HADOOP_CLASSPATH} -d '/home/cloudera/Desktop/HW2/TFIDF' '/home/cloudera/Desktop/HW2/TFIDF.java' 

In the above command the first path after -d is the folder in which the .class file for the same will be created after the compilation, the second path is of the .java file which needs to be compiled.

2. Once the .class file is created for the file, we need to build a jar file for the same which will be containing the .class file, to create the .jar file below is the command:

	jar -cvf TFIDF.jar -C '/home/cloudera/Desktop/HW2/TFIDF/' .

Make sure that you are into the folder in which you want to create the jar file (for me its the DocWordCount folder), after the above command is executed the .jar file will be created for us.

3. Once the jar file is create we need to run this jar file on Hadoop and get the output of our java file. For running the jar file use the below command:
 
	hadoop jar '/home/cloudera/Desktop/HW2/TFIDF/TFIDF.jar' TFIDF /user/cloudera/HW2/DocWordCount/input /user/cloudera/HW2/TFIDF/output /user/cloudera/HW2/TFIDF/output1

For the above command the path after the 'jar' is the path of the .jar file for the corresponding class, then comes the class name for the same .java file then is the input directory which will give the input to the Mapper and after that is the ouput directory which will contain the outpur of the MapReduce job. Note: There are two output directory paths as we are running two MapReduce Job in this code with TFIDF code in the second directory.

4. Once the .jar file is run using the above command the output will be genrated in the output directory. To view the output you can give the below command:
	
	$ hadoop dfs -cat /user/cloudera/HW2/TFIDF/output1/p*

In the above command the last argument is the path for the output directory. Since the output file which is generated has the name part-r-00000, hence p* is used to get the output of our .java file. This will display the output on the terminal.

5. Now to save the output there are two options as described below:

	a. Go to the output directory in your browser and download the file.
	b. In the terminal you can give the following command: hadoop fs -getmerge /user/cloudera/HW2/TFIDF/output1/p* TFIDF.out 
	 This output will create teh output file in the same folder as the user is currently in so, its better to be in the directory in which you want your output file to be saved.

							----- Instructions for Search.java file  ----- 
1. Compile your .java file using the following command:

	 $ javac -classpath {$HADOOP_CLASSPATH} -d '/home/cloudera/Desktop/HW2/Search/' '/home/cloudera/Desktop/HW2/Search.java' 

In the above command the first path after -d is the folder in which the .class file for the same will be created after the compilation, the second path is of the .java file which needs to be compiled.

2. Once the .class file is created for the file, we need to build a jar file for the same which will be containing the .class file, to create the .jar file below is the command:

	$ jar -cvf Search.jar -C '/home/cloudera/Desktop/HW2/Search/' .

Make sure that you are into the folder in which you want to create the jar file (for me its the DocWordCount folder), after the above command is executed the .jar file will be created for us.

3. Once the jar file is create we need to run this jar file on Hadoop and get the output of our java file. For running the jar file use the below command:
 
	$ hadoop jar '/home/cloudera/Desktop/HW2/Search/Search.jar' Search /user/cloudera/HW2/DocWordCount/input /user/cloudera/HW2/Search/output_q1

For the above command the path after the 'jar' is the path of the .jar file for the corresponding class, then comes the class name for the same .java file then is the input directory which will give the input to the Mapper and after that is the ouput directory which will contain the outpur of the MapReduce job. Once you provide this command there will be the prompt to 'Enter the String:' where user will provide the input query.

We will again run the above command since there are two user queries. Run the above command the only change will be in the output directory. Below is the command:

	$ hadoop jar '/home/cloudera/Desktop/HW2/Search/Search.jar' Search /user/cloudera/HW2/DocWordCount/input /user/cloudera/HW2/Search/output_q2

Once you provide this command there will be the prompt to 'Enter the String:' where user will provide the input query.

Note: The input path for this command is that of the TDIDF output.

4. Once the .jar file is run using the above command the output will be genrated in the output directory. To view the output you can give the below command:
	
	$ hadoop dfs -cat /user/cloudera/HW2/Search/output_q1/p*
	$ hadoop dfs -cat /user/cloudera/HW2/Search/output_q2/p*

In the above command the last argument is the path for the output directory. Since the output file which is generated has the name part-r-00000, hence p* is used to get the output of our .java file. This will display the output on the terminal.

Note: Give the above query one after the other.

5. Now to save the output there are two options as described below:

	a. Go to the output directory in your browser and download the file.
	b. In the terminal you can give the following command:
	 hadoop fs -getmerge /user/cloudera/HW2/Search/output_q1/p* query1.out 
	 hadoop fs -getmerge /user/cloudera/HW2/Search/output_q2/p* query2.out
	 
This output will create teh output file in the same folder as the user is currently in so, its better to be in the directory in which you want your output file to be saved.
