package com.android.server.privacy.impl.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class ConfigModel {
	private Map<String,PackageConfig> m_packageConfig = new TreeMap<String,PackageConfig>();

	public void writeXML(OutputStream out) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlSerializer ser = factory.newSerializer();
		
		ser.setOutput(out, "UTF-8");
		ser.startDocument("UTF-8", null);
		
		writeXML(ser);
		
		ser.flush();
	}

	private void writeXML(XmlSerializer ser) throws IOException {
		ser.startTag(null, "PrivacyConfig");
		
		ser.startTag(null, "Packages");
		
		for(PackageConfig c : m_packageConfig.values()) {
			c.writeXML(ser);
		}
		
		ser.endTag(null, "Packages");
		
		ser.endTag(null, "PrivacyConfig");
	}

	public void put(PackageConfig pack) {
		m_packageConfig.put(pack.getName(), pack);
	}

	public static ConfigModel readXML(InputStream in) throws XmlPullParserException, IOException {
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		
		parser.setInput(in, "UTF-8");

		parser.require(XmlPullParser.START_DOCUMENT, null, null);
		parser.next();
		
		ConfigModel res = readXML(parser);
		
		parser.require(XmlPullParser.END_DOCUMENT, null, null);
		return res;
	}

	private static ConfigModel readXML(XmlPullParser parser) throws XmlPullParserException, IOException {
		ConfigModel res = new ConfigModel();
		
		parser.require(XmlPullParser.START_TAG, null, "PrivacyConfig");
		parser.next();
		parser.require(XmlPullParser.START_TAG, null, "Packages");
		parser.next();
		while ( PackageConfig.canConsume(parser) ) {
			PackageConfig p = PackageConfig.readXML(parser);
			res.put(p);
		}
		parser.require(XmlPullParser.END_TAG, null, "Packages");
		parser.next();
		parser.require(XmlPullParser.END_TAG, null, "PrivacyConfig");
		parser.next();
		
		return res;
	}

	public void saveXML(File save) throws XmlPullParserException, IOException {
		File tmp = new File(save.getAbsolutePath() + System.currentTimeMillis() + ".tmp");
		FileOutputStream fout = new FileOutputStream(tmp);
		writeXML(fout);
		fout.close();
		if ( !tmp.renameTo(save) ) throw new IOException("can't rename " + tmp + " to " + save);
	}

	public static ConfigModel loadXML(File f) throws XmlPullParserException, IOException {
		FileInputStream fin = new FileInputStream(f);
		ConfigModel res = readXML(fin);
		fin.close();
		return res;
	}

	public List<String> getPackagesNames() {
		return new ArrayList<String>(m_packageConfig.keySet());
	}

	public PackageConfig getPackageConfig(String packageName) {
		return m_packageConfig.get(packageName);
	}

	public void removePackage(String packageName) {
		m_packageConfig.remove(packageName);
	}
	
	
}
