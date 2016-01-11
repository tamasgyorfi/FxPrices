cd EnvironmentService
echo "##### Starting EnvironmentData service"
scala -classpath target/staticdata-service.jar hu.environment.main.Server &
sleep 15
cd ../PersistenceService
echo "##### Starting Persistence Service"
scala -classpath target/persistence-service.jar hu.persistence.driver.PersistenceServiceStarter &
cd ../MonitoringService
echo "##### Staring Monitoring service"
java -cp target/monitoring-service.jar hu.fx.config.Main &
cd ../FxPricer
echo "##### Starting Price service"
scala -classpath target/prices-service.jar hu.fx.service.main.Server
cd ..
