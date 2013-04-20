package com.android.server.privacy.impl.model.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.android.server.privacy.impl.model.ConfigModel;
import com.android.server.privacy.impl.model.PackageConfig;

// standalone test class
public class ModelTester {

	public static void main(String[] args) throws Exception {
		ConfigModel cfg = new ConfigModel();
		
		PackageConfig pack = new PackageConfig("hase");
		pack.addRevocedPermission("nixMachen");
		pack.addRevocedPermission("toteHose");
		
		cfg.put(pack);
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		cfg.writeXML(bout);
		bout.writeTo(System.out);
		System.out.println();
		
		ConfigModel clone = ConfigModel.readXML(new ByteArrayInputStream(bout.toByteArray()));
		System.out.println("== CLONE ==");
		clone.writeXML(System.out);
		System.out.println();
		
		File f = File.createTempFile("dummy", ".xml");
		System.out.println("saving to:" + f);
		clone.saveXML(f);
		
		ConfigModel.loadXML(f);
		System.out.println("== CLONE ==");
		clone.writeXML(System.out);
		System.out.println();
	}

}
