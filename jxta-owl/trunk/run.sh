CLASSPATH=""
for i in $( ls lib/*.jar );
do 
CLASSPATH="$CLASSPATH$i:" 
done
java -cp $CLASSPATH com.dumontierlab.jxta.owl.diag.ServerTest