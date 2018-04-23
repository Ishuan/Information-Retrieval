/* Ishan Agarwal email: iagarwa1@uncc.edu */

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;


public class TFIDF extends Configured implements Tool {

	private static final Logger LOG = Logger .getLogger( TFIDF.class);

	public static void main( String[] args) throws  Exception {
		int res  = ToolRunner .run( new TFIDF(), args);
		System .exit(res);
	}

	public int run( String[] args) throws  Exception {
		Job job  = Job .getInstance(getConf(), " wordcount ");
		job.setJarByClass( this .getClass());
		FileInputFormat.addInputPaths(job,  args[0]);                           //Mapper will take the input from this location - this is for MapReduce Job1
		FileOutputFormat.setOutputPath(job,  new Path(args[ 1]));               //Reducer will give the output at this location - this is for MapReduce Job1
		job.setMapperClass( Map .class);
		job.setReducerClass( Reduce .class);
		job.setOutputKeyClass( Text .class);
		job.setOutputValueClass( DoubleWritable .class);
		job.waitForCompletion( true);

		Configuration conf= new Configuration();
		FileSystem fs = FileSystem.get(conf);
		int Total_no_of_docs=0;													//This for total number of docs/files on which our jobs will run
		boolean a = false;

		/* Below is the logic for calculating the number of files provided as the input Line 54-58*/

		RemoteIterator<LocatedFileStatus> b = fs.listFiles(new Path(args[0]),a); 
		while(b.hasNext()){
			Total_no_of_docs++;
			b.next();
		}


		conf.setInt("documents", Total_no_of_docs);  // The parameter of total number of files are passed to the MapReducer job 
		System.out.println(Total_no_of_docs);

		Job job2  = Job .getInstance(conf, " wordcount1");
		job2.setJarByClass( this .getClass());
		FileInputFormat.addInputPaths(job2,  args[1]);								//Mapper will take the input from this location - this is for MapReduce Job2
		FileOutputFormat.setOutputPath(job2,  new Path(args[2]));				    //Reducer will give the output at this location - this is for MapReduce Job2
		job2.setMapperClass( Map2 .class);
		job2.setReducerClass( Reduce2 .class);
		job2.setOutputKeyClass( Text .class);
		job2.setOutputValueClass( Text .class);

		return job2.waitForCompletion( true)  ? 0 : 1;
	}

	public static class Map extends Mapper<LongWritable ,  Text ,  Text ,  DoubleWritable > {
		private final static DoubleWritable one  = new DoubleWritable( 1);
		private Text word  = new Text();

		private static final Pattern WORD_BOUNDARY = Pattern .compile("\\s*\\b\\s*");

		public void map( LongWritable offset,  Text lineText,  Context context)
				throws  IOException,  InterruptedException {

			String line  = lineText.toString();  						//Converting the Text type of Map function to String type
			Text currentWord  = new Text();

			String filename = ((FileSplit)context.getInputSplit()).getPath().getName();  		//Getting the filename of the file which is providing input to the Mapper

			for ( String word  : WORD_BOUNDARY .split(line)) {
				if (word.isEmpty()) {
					continue;
				}
				currentWord  = new Text((word.toLowerCase())+"#####"+filename);     // currentword will be containing the format that will be given as the input to the reducer, e.g. Hadoop#####file1.txt
				context.write(currentWord,one);						// this will be the output of the Mapper which will be sent to the Reducer
			}
		}
	}

	public static class Reduce extends Reducer<Text ,  DoubleWritable ,  Text ,  DoubleWritable > {

		public void reduce( Text word,  Iterable<DoubleWritable > counts,  Context context)
				throws IOException,  InterruptedException {
			int sum  = 0;
			for ( DoubleWritable count  : counts) {
				sum  += count.get();								//To calculate the total occurrences of a word in a file
			}

			double term_freq = 1 + Math.log10(sum);					//To calculate the term frequency of the word
			context.write(word,new DoubleWritable(term_freq));		// This will be the output of the Reducer of the format (e.g.: Hadoop#####file1.txt 1.0)
		}
	}

	/* The second MapReduce Job starts from here */

	public static class Map2 extends Mapper<LongWritable ,  Text ,  Text ,  Text > {

		public void map( LongWritable offset,  Text lineText,  Context context)
				throws  IOException,  InterruptedException {
			String line = lineText.toString();

			String arr[] = line.split("#####");						//This will split the output of the first MapReduce Job based on #'s
			String subarr[] = arr[1].split("\t");					//The string that was split on the basis of # are further split on tabs.

			Text key = new Text();
			Text value = new Text();
			key = new Text(arr[0]);
			value = new Text(subarr[0]+"="+subarr[1]);				//The file name and term frequency are concatenated and are sent in the format filename=termfrequency
			context.write(key,value);								//The output of the Mapper for MR2 sent in the format of <key, value> pair. e.g. <word, filename=termfrequency

		}

	}

	public static class Reduce2 extends Reducer<Text ,  Text ,  Text ,  DoubleWritable > {
		public void reduce( Text word,  Iterable<Text> counts,  Context context)
				throws IOException,  InterruptedException {

			int Total_no_of_docs = context.getConfiguration().getInt("documents", 0); //Total numnber of documents are provided to the reducer.
			Text name1 = new Text();
			double tf_idf=0;

			int num_of_docs=0;
			HashMap<String,Double> mp = new HashMap<String,Double>();     //Hash Map will be used to retrieve the term frequency against the particular file for a particular word

			/* the below loop will caluclate the docs in which the term occurs and will also store the term freuquency for a particular file for a particular word */
			
			for(Text txt: counts){
				String[] tt = txt.toString().split("=");
				String fname = tt[0];
				String termfr = tt[1];
				num_of_docs++;
				mp.put(fname, Double.valueOf(termfr));
			}

			double idf= Math.log10(1+(Total_no_of_docs/num_of_docs));     // Calculating the idf using the formula provided in the pdf.
			
			/* Below loop is used to calculate the TF_IDF */

			for(String txt2:mp.keySet()){
				tf_idf = mp.get(txt2) * idf;
				name1 = new Text(word+"#####"+txt2);
				context.write(name1,new DoubleWritable(tf_idf));
			}
		}
	}
}


