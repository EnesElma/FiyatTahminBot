
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;

public class Prediction {
    public SparkSession s;
    public PipelineModel p;

    public void startAndLoad(){
        Logger.getLogger("org").setLevel(Level.ERROR);
        final SparkSession spark = SparkSession.builder().appName("TelegramPredictionBot").master("local").getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
        final PipelineModel pipelineModel=PipelineModel.load("gbtRegression.modelPipelineFinal");
        s=spark;
        p=pipelineModel;
    }


    public int predict(String il, String ilce, String oda, int m2){
        ArrayList<Row> list=new ArrayList<Row>();
        list.add(RowFactory.create(m2,oda.trim(),il.trim(),ilce.trim()));
        ArrayList<org.apache.spark.sql.types.StructField> listOfStructField=new ArrayList<org.apache.spark.sql.types.StructField>();
        listOfStructField.add(DataTypes.createStructField("m2", DataTypes.IntegerType, true));
        listOfStructField.add(DataTypes.createStructField("oda", DataTypes.StringType, true));
        listOfStructField.add(DataTypes.createStructField("il", DataTypes.StringType, true));
        listOfStructField.add(DataTypes.createStructField("ilce", DataTypes.StringType, true));
        StructType structType=DataTypes.createStructType(listOfStructField);
        Dataset<Row> data=s.createDataFrame(list,structType);

        Dataset<Row> dfx=p.transform(data).select("m2","oda","il","ilce","prediction");
        Double y = null;
        Row row=dfx.select("prediction").as(String.valueOf(y)).collectAsList().get(0);
        int d=(int)(Double.parseDouble(row.get(0).toString()));
        int x=(d/1000)*1000;
        return x;
    }
}
