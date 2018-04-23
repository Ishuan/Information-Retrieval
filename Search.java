
/* Ishan Agarwal email: iagarwa1@uncc.edu */

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;


public class Search extends Configured implements Tool {

	private static final Logger LOG = Logger .getLogger( Search.class);

	public static void main( String[] args) throws  Exception {
		int res  = ToolRunner .run( new Search(), args);
		System .exit(res);
	}

	public int run( String[] args) throws  Exception {

		System.out.println("Enter the String:");    //Asks the user to input the string
		Scanner scanner = new Scanner(System.in);   

		String str = scanner.nextLine();			//Scanner is used to read the input from the command line

		
		/* Below is the logic for providing the input string to the Mapper Line: 45-47 */
		 
		Configuration c = new Configuration();
		c.set("input_string",str);
		Job job  = Job .getInstance(c, " wordcount ");
		
		job.setJarByClass( this .getClass());

		FileInputFormat.addInputPaths(job,  args[0]);  				//Mapper will take the input from this location
		FileOutputFormat.setOutputPath(job,  new Path(args[ 1]));	//Reducer will give the output at this location
		job.setMapperClass( Map .class);
		job.setReducerClass( Reduce .class);
		job.setOutputKeyClass( Text .class);
		job.setOutputValueClass( DoubleWritable .class);

		return job.waitForCompletion( true)  ? 0 : 1;
	}

	public static class Map extends Mapper<LongWritable ,  Text ,  Text ,  DoubleWritable > {
		private final static IntWritable one  = new IntWritable( 1);

		private Text word  = new Text();
		private static final Pattern WORD_BOUNDARY = Pattern .compile("\\s*\\b\\s*");

		public void map( LongWritable offset,  Text lineText,  Context context)
				throws  IOException,  InterruptedException {

			String user_input=context.getConfiguration().get("input_string");  //This will store the input provided by the user

			String[] str = user_input.split(" ");   //This will split the input string based on spaces

			/* This is the logic which will check if the words in the input query is present in the output of TFIDF Line: 76-83 */
			String[] st1 = lineText.toString().split("#####");
			String[] st2 = st1[1].split("\t");
			for(int i=0;i<str.length;i++){
				if(st1[0].equals(str[i]))
				{
				
					context.write(new Text(st2[0]), new DoubleWritable(Double.valueOf(st2[1])));  //The output of Mapper in the format filename TFIDF
				}
			}
		}
	}

	public static class Reduce extends Reducer<Text ,  DoubleWritable ,  Text ,  DoubleWritable > {
		@Override 
		public void reduce( Text word,  Iterable<DoubleWritable > counts,  Context context)
				throws IOException,  InterruptedException {
			double sum  = 0;
			for ( DoubleWritable count  : counts) {
				sum  += count.get(); 						//This will sum up the TFIDF for that word in all the files
			}

			context.write(word,  new DoubleWritable(sum));
		}
	}
}
