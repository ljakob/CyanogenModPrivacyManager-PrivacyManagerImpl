package com.android.server.privacy.impl.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class PackageConfig {
	private String m_name;
	private Set<String> m_revokedPermissions = new TreeSet<String>();
	
	public PackageConfig() {
		
	}

	public PackageConfig(String name) {
		m_name = name;
	}

	public void addRevocedPermission(String name) {
		m_revokedPermissions.add(name);
	}

	public String getName() {
		return m_name;
	}

	public void writeXML(XmlSerializer ser) throws IOException {
		
		if ( ! hasData() ) return;
		
		ser.startTag(null, "Package");
		ser.attribute(null, "name", m_name);
		
		for(String p : m_revokedPermissions) {
			ser.startTag(null, "RevokedPermission");
			ser.attribute(null, "name", p);
			ser.endTag(null, "RevokedPermission");
		}
		
		ser.endTag(null, "Package");
	}

	private boolean hasData() {
		return !m_revokedPermissions.isEmpty();
	}

	public static PackageConfig readXML(XmlPullParser parser) throws XmlPullParserException, IOException {
		PackageConfig res = new PackageConfig();
		
		parser.require(XmlPullParser.START_TAG, null, "Package");
		res.m_name = parser.getAttributeValue(null, "name");
		parser.next();

		while(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("RevokedPermission") ) {
			parser.next();
			res.m_revokedPermissions.add(parser.getAttributeValue(null, "name"));
			parser.require(XmlPullParser.END_TAG, null, "RevokedPermission");
			parser.next();
		}
		
		parser.require(XmlPullParser.END_TAG, null, "Package");
		parser.next();
		
		return res;
	}

	public static boolean canConsume(XmlPullParser parser) throws XmlPullParserException, IOException {
		return parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Package");
	}

	public List<String> getRevokedPermissions() {
		return new ArrayList<String>(m_revokedPermissions);
	}

	public void setRevokedPermissions(Collection<String> permissions) {
		m_revokedPermissions = new TreeSet<String>(permissions);
	}

	public boolean isPermissionRevoked(String permName) {
		if ( permName == null ) return false;
		return m_revokedPermissions.contains(permName);
	}
	
}
