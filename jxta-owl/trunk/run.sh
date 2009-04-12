CLASSPATH=""
for i in $( ls lib/*.jar );
do 
CLASSPATH="$CLASSPATH$i:" 
done



java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n -cp $CLASSPATH com.dumontierlab.jxta.owl.diag.ServerTest