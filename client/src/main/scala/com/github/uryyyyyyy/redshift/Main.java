package com.github.uryyyyyyy.redshift;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;

public class Main {

	public static void main(String[] args){
		AerospikeClient client = new AerospikeClient("172.17.0.3", 3000);

		Key key = new Key("test", "demo", "putgetkey");
		Bin bin1 = new Bin("bin1", "value1");
		Bin bin2 = new Bin("bin2", "value2");

// Write a record
		client.put(null, key, bin1, bin2);

// Read a record
		Record record = client.get(null, key);
		System.out.println(record.bins.get("bin1"));
		System.out.println(record.bins.get("bin2"));

		client.close();
	}
}
