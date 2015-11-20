cd StaticDataService
echo "##### Starting StaticData service"
scala -classpath target/staticdata-service.jar hu.staticdata.StaticDataServer &
sleep 15
cd ../MessageBrokerWrapper
echo "##### Starting MQWrapper service"
scala -classpath target/messagebroker-service.jar hu.messagebroker.ActiveMQBrokerStarter > /dev/null &
sleep 5
cd ../PersistenceService
echo "##### Starting Persistence Service"
scala -classpath target/persistence-service.jar hu.persistence.MessageReceiverFactory &
cd ../FxPricer
echo "##### Starting Price service"
scala -classpath target/prices-service.jar hu.fx.service.main.Server
cd ..
