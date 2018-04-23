/* Ishan Agarwal email: iagarwa1@uncc.edu */

import java.io.IOException;
import java.util.regex.Pattern;

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


public class TermFrequency extends Configured implements Tool {

   private static final Logger LOG = Logger .getLogger( TermFrequency.class);

   public static void main( String[] args) throws  Exception {
      int res  = ToolRunner .run( new TermFrequency(), args);
      System .exit(res);
   }

   public int run( String[] args) throws  Exception {
      Job job  = Job .getInstance(getConf(), " wordcount ");
      job.setJarByClass( this .getClass());

      FileInputFormat.addInputPaths(job,  args[0]);                     //Mapper will take the input from this location
      FileOutputFormat.setOutputPath(job,  new Path(args[ 1]));         //Reducer will give the output at this location
      job.setMapperClass( Map .class);
      job.setReducerClass( Reduce .class);
      job.setOutputKeyClass( Text .class);
      job.setOutputValueClass( IntWritable .class);

      return job.waitForCompletion( true)  ? 0 : 1;
   }
   
   public static class Map extends Mapper<LongWritable ,  Text ,  Text ,  IntWritable > {
      private final static IntWritable one  = new IntWritable( 1);
      private Text word  = new Text();
      
      private static final Pattern WORD_BOUNDARY = Pattern .compile("\\s*\\b\\s*");

      public void map( LongWritable offset,  Text lineText,  Context context)
        throws  IOException,  InterruptedException {

         String line  = lineText.toString();       //Converting the Text type of Map function to String type
         Text currentWord  = new Text();
         
       		String  filename = ((FileSplit)context.getInputSplit()).getPath().getName(); //Getting the filename of the file which is providing input to the Mapper
         
         for ( String word  : WORD_BOUNDARY .split(line)) {
            if (word.isEmpty()) {
               continue;
            }
            currentWord  = new Text((word.toLowerCase())+"#####"+filename+"\t");   // currentword will be containing the format that will be given as the input to the reducer, e.g. Hadoop#####file1.txt
            context.write(currentWord,one);                        // this will be the output of the Mapper which will be sent to the Reducer
         }
      }
   }

   public static class Reduce extends Reducer<Text ,  IntWritable ,  Text ,  DoubleWritable > {
      @Override 
      public void reduce( Text word,  Iterable<IntWritable > counts,  Context context)
         throws IOException,  InterruptedException {
         int sum  = 0;
	
         for ( IntWritable count  : counts) {
            sum  += count.get();                                     //To calculate the total occurrences of a word in a file
         }

         double term_freq = 1 + Math.log10(sum);                     //To calculate the terminal frequency of the word
         
         context.write(word, new DoubleWritable(term_freq));         // This will be the output of the Reducer of the format (e.g.: Hadoop#####file1.txt 1.0)
      }
   }
}
