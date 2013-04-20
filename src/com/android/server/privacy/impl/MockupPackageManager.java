package com.android.server.privacy.impl;

import java.util.List;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ContainerEncryptionParams;
import android.content.pm.FeatureInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageMoveObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.InstrumentationInfo;
import android.content.pm.ManifestDigest;
import android.content.pm.PackageCleanItem;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.content.pm.VerificationParams;
import android.content.pm.VerifierDeviceIdentity;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.server.privacy.impl.model.ConfigModel;
import com.android.server.privacy.impl.model.PackageConfig;

public class MockupPackageManager extends android.content.pm.IPackageManager.Stub {

	private Context m_ctx;
	private IPackageManager m_pm;
	private ConfigModel m_cfg;
	
	public MockupPackageManager(Context ctx, ConfigModel cfg) {
		m_ctx = ctx;
		m_pm = AppGlobals.getPackageManager();
		m_cfg = cfg;
	}

	public int checkPermission(String permName, String pkgName) throws RemoteException {
		int res = m_pm.checkPermission(permName, pkgName);
		if ( res == PackageManager.PERMISSION_DENIED ) {
			PackageConfig cfg = m_cfg.getPackageConfig(pkgName);
			if ( cfg != null && cfg.isPermissionRevoked(permName) ) {
				return PackageManager.PERMISSION_GRANTED;
			}
		}
		return res;
	}

	public int checkUidPermission(String permName, int uid) throws RemoteException {
		int res = m_pm.checkUidPermission(permName, uid);
		
		if ( res == PackageManager.PERMISSION_DENIED ) {
            String[] packages = m_pm.getPackagesForUid(uid);
            if ( packages == null ) return res;
            for(String p : packages) {
            	PackageConfig cfg = m_cfg.getPackageConfig(p);
    			if ( cfg != null && cfg.isPermissionRevoked(permName) ) {
    				return PackageManager.PERMISSION_GRANTED;
    			}
            }
		}		
		
		return res;
	}

    public IBinder asBinder() {
        return m_pm.asBinder();
    }

    public PackageInfo getPackageInfo(String packageName, int flags, int userId)
            throws RemoteException {
        return m_pm.getPackageInfo(packageName, flags, userId);
    }

    public int getPackageUid(String packageName, int userId) throws RemoteException {
        return m_pm.getPackageUid(packageName, userId);
    }

    public int[] getPackageGids(String packageName) throws RemoteException {
        return m_pm.getPackageGids(packageName);
    }

    public String[] currentToCanonicalPackageNames(String[] names) throws RemoteException {
        return m_pm.currentToCanonicalPackageNames(names);
    }

    public String[] canonicalToCurrentPackageNames(String[] names) throws RemoteException {
        return m_pm.canonicalToCurrentPackageNames(names);
    }

    public PermissionInfo getPermissionInfo(String name, int flags) throws RemoteException {
        return m_pm.getPermissionInfo(name, flags);
    }

    public List<PermissionInfo> queryPermissionsByGroup(String group, int flags)
            throws RemoteException {
        return m_pm.queryPermissionsByGroup(group, flags);
    }

    public PermissionGroupInfo getPermissionGroupInfo(String name, int flags)
            throws RemoteException {
        return m_pm.getPermissionGroupInfo(name, flags);
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) throws RemoteException {
        return m_pm.getAllPermissionGroups(flags);
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId)
            throws RemoteException {
        return m_pm.getApplicationInfo(packageName, flags, userId);
    }

    public ActivityInfo getActivityInfo(ComponentName className, int flags, int userId)
            throws RemoteException {
        return m_pm.getActivityInfo(className, flags, userId);
    }

    public ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId)
            throws RemoteException {
        return m_pm.getReceiverInfo(className, flags, userId);
    }

    public ServiceInfo getServiceInfo(ComponentName className, int flags, int userId)
            throws RemoteException {
        return m_pm.getServiceInfo(className, flags, userId);
    }

    public ProviderInfo getProviderInfo(ComponentName className, int flags, int userId)
            throws RemoteException {
        return m_pm.getProviderInfo(className, flags, userId);
    }

    public boolean addPermission(PermissionInfo info) throws RemoteException {
        return m_pm.addPermission(info);
    }

    public void removePermission(String name) throws RemoteException {
        m_pm.removePermission(name);
    }

    public void grantPermission(String packageName, String permissionName) throws RemoteException {
        m_pm.grantPermission(packageName, permissionName);
    }

    public void revokePermission(String packageName, String permissionName) throws RemoteException {
        m_pm.revokePermission(packageName, permissionName);
    }

    public boolean isProtectedBroadcast(String actionName) throws RemoteException {
        return m_pm.isProtectedBroadcast(actionName);
    }

    public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
        return m_pm.checkSignatures(pkg1, pkg2);
    }

    public int checkUidSignatures(int uid1, int uid2) throws RemoteException {
        return m_pm.checkUidSignatures(uid1, uid2);
    }

    public String[] getPackagesForUid(int uid) throws RemoteException {
        return m_pm.getPackagesForUid(uid);
    }

    public String getNameForUid(int uid) throws RemoteException {
        return m_pm.getNameForUid(uid);
    }

    public int getUidForSharedUser(String sharedUserName) throws RemoteException {
        return m_pm.getUidForSharedUser(sharedUserName);
    }

    public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId)
            throws RemoteException {
        return m_pm.resolveIntent(intent, resolvedType, flags, userId);
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags,
            int userId) throws RemoteException {
        return m_pm.queryIntentActivities(intent, resolvedType, flags, userId);
    }

    public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller, Intent[] specifics,
            String[] specificTypes, Intent intent, String resolvedType, int flags, int userId)
            throws RemoteException {
        return m_pm.queryIntentActivityOptions(caller, specifics, specificTypes, intent,
                resolvedType, flags, userId);
    }

    public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags,
            int userId) throws RemoteException {
        return m_pm.queryIntentReceivers(intent, resolvedType, flags, userId);
    }

    public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId)
            throws RemoteException {
        return m_pm.resolveService(intent, resolvedType, flags, userId);
    }

    public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags,
            int userId) throws RemoteException {
        return m_pm.queryIntentServices(intent, resolvedType, flags, userId);
    }

    public ParceledListSlice getInstalledPackages(int flags, String lastRead, int userId)
            throws RemoteException {
        return m_pm.getInstalledPackages(flags, lastRead, userId);
    }

    public List<PackageInfo> getInstalledThemePackages() throws RemoteException {
        return m_pm.getInstalledThemePackages();
    }

    public ParceledListSlice getInstalledApplications(int flags, String lastRead, int userId)
            throws RemoteException {
        return m_pm.getInstalledApplications(flags, lastRead, userId);
    }

    public List<ApplicationInfo> getPersistentApplications(int flags) throws RemoteException {
        return m_pm.getPersistentApplications(flags);
    }

    public ProviderInfo resolveContentProvider(String name, int flags, int userId)
            throws RemoteException {
        return m_pm.resolveContentProvider(name, flags, userId);
    }

    public void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo)
            throws RemoteException {
        m_pm.querySyncProviders(outNames, outInfo);
    }

    public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags)
            throws RemoteException {
        return m_pm.queryContentProviders(processName, uid, flags);
    }

    public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags)
            throws RemoteException {
        return m_pm.getInstrumentationInfo(className, flags);
    }

    public List<InstrumentationInfo> queryInstrumentation(String targetPackage, int flags)
            throws RemoteException {
        return m_pm.queryInstrumentation(targetPackage, flags);
    }

    public void installPackage(Uri packageURI, IPackageInstallObserver observer, int flags,
            String installerPackageName) throws RemoteException {
        m_pm.installPackage(packageURI, observer, flags, installerPackageName);
    }

    public void finishPackageInstall(int token) throws RemoteException {
        m_pm.finishPackageInstall(token);
    }

    public void setInstallerPackageName(String targetPackage, String installerPackageName)
            throws RemoteException {
        m_pm.setInstallerPackageName(targetPackage, installerPackageName);
    }

    public void deletePackage(String packageName, IPackageDeleteObserver observer, int flags)
            throws RemoteException {
        m_pm.deletePackage(packageName, observer, flags);
    }

    public String getInstallerPackageName(String packageName) throws RemoteException {
        return m_pm.getInstallerPackageName(packageName);
    }

    public void addPackageToPreferred(String packageName) throws RemoteException {
        m_pm.addPackageToPreferred(packageName);
    }

    public void removePackageFromPreferred(String packageName) throws RemoteException {
        m_pm.removePackageFromPreferred(packageName);
    }

    public List<PackageInfo> getPreferredPackages(int flags) throws RemoteException {
        return m_pm.getPreferredPackages(flags);
    }

    public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set,
            ComponentName activity, int userId) throws RemoteException {
        m_pm.addPreferredActivity(filter, match, set, activity, userId);
    }

    public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set,
            ComponentName activity) throws RemoteException {
        m_pm.replacePreferredActivity(filter, match, set, activity);
    }

    public void clearPackagePreferredActivities(String packageName) throws RemoteException {
        m_pm.clearPackagePreferredActivities(packageName);
    }

    public int getPreferredActivities(List<IntentFilter> outFilters,
            List<ComponentName> outActivities, String packageName) throws RemoteException {
        return m_pm.getPreferredActivities(outFilters, outActivities, packageName);
    }

    public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags,
            int userId) throws RemoteException {
        m_pm.setComponentEnabledSetting(componentName, newState, flags, userId);
    }

    public int getComponentEnabledSetting(ComponentName componentName, int userId)
            throws RemoteException {
        return m_pm.getComponentEnabledSetting(componentName, userId);
    }

    public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId)
            throws RemoteException {
        m_pm.setApplicationEnabledSetting(packageName, newState, flags, userId);
    }

    public int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
        return m_pm.getApplicationEnabledSetting(packageName, userId);
    }

    public void setPackageStoppedState(String packageName, boolean stopped, int userId)
            throws RemoteException {
        m_pm.setPackageStoppedState(packageName, stopped, userId);
    }

    public void freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer)
            throws RemoteException {
        m_pm.freeStorageAndNotify(freeStorageSize, observer);
    }

    public void freeStorage(long freeStorageSize, IntentSender pi) throws RemoteException {
        m_pm.freeStorage(freeStorageSize, pi);
    }

    public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer)
            throws RemoteException {
        m_pm.deleteApplicationCacheFiles(packageName, observer);
    }

    public void clearApplicationUserData(String packageName, IPackageDataObserver observer,
            int userId) throws RemoteException {
        m_pm.clearApplicationUserData(packageName, observer, userId);
    }

    public void getPackageSizeInfo(String packageName, int userHandle,
            IPackageStatsObserver observer) throws RemoteException {
        m_pm.getPackageSizeInfo(packageName, userHandle, observer);
    }

    public String[] getSystemSharedLibraryNames() throws RemoteException {
        return m_pm.getSystemSharedLibraryNames();
    }

    public FeatureInfo[] getSystemAvailableFeatures() throws RemoteException {
        return m_pm.getSystemAvailableFeatures();
    }

    public boolean hasSystemFeature(String name) throws RemoteException {
        return m_pm.hasSystemFeature(name);
    }

    public void enterSafeMode() throws RemoteException {
        m_pm.enterSafeMode();
    }

    public boolean isSafeMode() throws RemoteException {
        return m_pm.isSafeMode();
    }

    public void systemReady() throws RemoteException {
        m_pm.systemReady();
    }

    public boolean hasSystemUidErrors() throws RemoteException {
        return m_pm.hasSystemUidErrors();
    }

    public void performBootDexOpt() throws RemoteException {
        m_pm.performBootDexOpt();
    }

    public boolean performDexOpt(String packageName) throws RemoteException {
        return m_pm.performDexOpt(packageName);
    }

    public void updateExternalMediaStatus(boolean mounted, boolean reportStatus)
            throws RemoteException {
        m_pm.updateExternalMediaStatus(mounted, reportStatus);
    }

    public PackageCleanItem nextPackageToClean(PackageCleanItem lastPackage) throws RemoteException {
        return m_pm.nextPackageToClean(lastPackage);
    }

    public void movePackage(String packageName, IPackageMoveObserver observer, int flags)
            throws RemoteException {
        m_pm.movePackage(packageName, observer, flags);
    }

    public boolean addPermissionAsync(PermissionInfo info) throws RemoteException {
        return m_pm.addPermissionAsync(info);
    }

    public boolean setInstallLocation(int loc) throws RemoteException {
        return m_pm.setInstallLocation(loc);
    }

    public int getInstallLocation() throws RemoteException {
        return m_pm.getInstallLocation();
    }

    public void installPackageWithVerification(Uri packageURI, IPackageInstallObserver observer,
            int flags, String installerPackageName, Uri verificationURI,
            ManifestDigest manifestDigest, ContainerEncryptionParams encryptionParams)
            throws RemoteException {
        m_pm.installPackageWithVerification(packageURI, observer, flags, installerPackageName,
                verificationURI, manifestDigest, encryptionParams);
    }

    public void installPackageWithVerificationAndEncryption(Uri packageURI,
            IPackageInstallObserver observer, int flags, String installerPackageName,
            VerificationParams verificationParams, ContainerEncryptionParams encryptionParams)
            throws RemoteException {
        m_pm.installPackageWithVerificationAndEncryption(packageURI, observer, flags,
                installerPackageName, verificationParams, encryptionParams);
    }

    public int installExistingPackage(String packageName) throws RemoteException {
        return m_pm.installExistingPackage(packageName);
    }

    public void verifyPendingInstall(int id, int verificationCode) throws RemoteException {
        m_pm.verifyPendingInstall(id, verificationCode);
    }

    public void extendVerificationTimeout(int id, int verificationCodeAtTimeout,
            long millisecondsToDelay) throws RemoteException {
        m_pm.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
    }

    public VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
        return m_pm.getVerifierDeviceIdentity();
    }

    public boolean isFirstBoot() throws RemoteException {
        return m_pm.isFirstBoot();
    }

    public void setPermissionEnforced(String permission, boolean enforced) throws RemoteException {
        m_pm.setPermissionEnforced(permission, enforced);
    }

    public boolean isPermissionEnforced(String permission) throws RemoteException {
        return m_pm.isPermissionEnforced(permission);
    }

    public boolean isStorageLow() throws RemoteException {
        return m_pm.isStorageLow();
    }

// dumb
	
}
