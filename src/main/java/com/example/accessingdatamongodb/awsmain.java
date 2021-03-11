package com.example.accessingdatamongodb;
import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;


public class awsmain {




        public static void main(String[] args) {
            MongoClientOptions.Builder options = MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true);

            String username = "admindb";
            String password = "admin001";
            String clusterEndpoint = "localhost:27017";
            String readPreference = "secondaryPreferred";
            String template = "mongodb://%s:%s@%s/sampledatabase?ssl=true&replicaSet=rs0&readpreference=%s";
            String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

            String truststore = "C:\\swtools\\mongo_shell\\rds-truststore.jks";
            String truststorePassword = "12345678";

            System.setProperty("javax.net.ssl.trustStore", truststore);
            System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

            MongoClientURI clientURI = new MongoClientURI(connectionString,options);
            MongoClient mongoClient = new MongoClient(clientURI);

            MongoDatabase testDB = mongoClient.getDatabase("sampledatabase");
            MongoCollection<Document> numbersCollection = testDB.getCollection("samplecollection");

            Document doc = new Document("name", "pi").append("value", 3.14159);
            numbersCollection.insertOne(doc);

            MongoCursor<Document> cursor = numbersCollection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }
        }
}
