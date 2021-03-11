package com.example.accessingdatamongodb;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;


public final class Main {
    private Main() {
    }

    // forwarding ports
    private static final String LOCAL_HOST = "localhost";
    private static final String REMOTE_HOST = "ec2-54-234-207-36.compute-1.amazonaws.com";
    private static final Integer LOCAL_PORT = 8988;
    private static final Integer REMOTE_PORT = 27017; // Default mongodb port

    // ssh connection info
    private static final String SSH_USER = "ec2-user";
    private static final String SSH_PASSWORD = "<password>";
    private static final String SSH_HOST = "<remote host>";
    private static final Integer SSH_PORT = 22;

    private static Session SSH_SESSION;

    public static void  main(){
        MongoClientOptions.Builder options = MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true);
        // add more options to the builder with your config
        MongoClient mongoClient = new MongoClient("localhost", options.build());
    }
    public static void main(String[] args) {
        MongoClientOptions.Builder options = MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true);

        String template = "mongodb://%s:%s@%s";
        String username = "admindb";
        String password = "admin001";
        String clusterEndpoint = "localhost:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = "mongodb://admindb:admin001@localhost:27017/?ssl=true";

        String truststore = "/c/swtools/mongo_shell/rds-truststore.jks'";
        String truststorePassword = "123456781";

        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        System.setProperty ("javax.net.ssl.trustStore","C:\\swtools\\mongo_shell\\lib\\security\\cacerts");
        System.setProperty ("javax.net.ssl.trustStorePassword","123456781");
        System.setProperty ("javax.net.ssl.keyStore", "C:\\swtools\\jdk-11.0.10\\lib\\security\\mongodb.pkcs12");
        System.setProperty ("javax.net.ssl.keyStorePassword","changeit");

      //  MongoClient mongoClient = new MongoClient( "localhost:27017" , options.build() );
        MongoClientURI clientURI = new MongoClientURI(connectionString,options);
        MongoClient mongoClient = new MongoClient(clientURI);

        MongoDatabase testDB = mongoClient.getDatabase("sampledatabase");
        MongoCollection<Document> numbersCollection = testDB.getCollection("sample-collection");

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
