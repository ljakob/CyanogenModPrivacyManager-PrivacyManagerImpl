package com.android.server.privacy.impl;

import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.TaskThumbnails;
import android.app.ActivityManagerNative;
import android.app.ApplicationErrorReport.CrashInfo;
import android.app.IActivityController;
import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.IInstrumentationWatcher;
import android.app.IProcessObserver;
import android.app.IServiceConnection;
import android.app.IStopUserCallback;
import android.app.IThumbnailReceiver;
import android.app.IUserSwitchObserver;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;
import android.util.Slog;

import com.android.server.privacy.impl.model.ConfigModel;

import java.util.List;

public class MockupActivityManager extends ActivityManagerNative {

	private IActivityManager m_orig;
	private Context m_ctx;
	private ConfigModel m_cfg;
	private IPackageManager m_pm;

	public MockupActivityManager(Context context, IPackageManager pm, ConfigModel cfg) {
		m_ctx = context;
		m_cfg = cfg;
		m_pm = pm;
		m_orig = ActivityManagerNative.getDefault();
	}

	@Override
	public int checkPermission(String permission, int pid, int uid) throws RemoteException {
		Slog.d(PrivacyManagerImpl.TAG, "checkPermission " + permission);

		int res = m_orig.checkPermission(permission, pid, uid);
		
		if ( res == PackageManager.PERMISSION_DENIED ) {
			res = m_pm.checkUidPermission(permission, uid);
		}
		
		return res;
	}

    public IBinder asBinder() {
        return m_orig.asBinder();
    }

    public int startActivity(IApplicationThread caller, Intent intent, String resolvedType,
            IBinder resultTo, String resultWho, int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options) throws RemoteException {
        return m_orig.startActivity(caller, intent, resolvedType, resultTo, resultWho, requestCode,
                flags, profileFile, profileFd, options);
    }

    public int startActivityAsUser(IApplicationThread caller, Intent intent, String resolvedType,
            IBinder resultTo, String resultWho, int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options, int userId) throws RemoteException {
        return m_orig.startActivityAsUser(caller, intent, resolvedType, resultTo, resultWho,
                requestCode, flags, profileFile, profileFd, options, userId);
    }

    public WaitResult startActivityAndWait(IApplicationThread caller, Intent intent,
            String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags,
            String profileFile, ParcelFileDescriptor profileFd, Bundle options, int userId)
            throws RemoteException {
        return m_orig.startActivityAndWait(caller, intent, resolvedType, resultTo, resultWho,
                requestCode, flags, profileFile, profileFd, options, userId);
    }

    public int startActivityWithConfig(IApplicationThread caller, Intent intent,
            String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int startFlags, Configuration newConfig, Bundle options, int userId)
            throws RemoteException {
        return m_orig.startActivityWithConfig(caller, intent, resolvedType, resultTo, resultWho,
                requestCode, startFlags, newConfig, options, userId);
    }

    public int startActivityIntentSender(IApplicationThread caller, IntentSender intent,
            Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
        return m_orig.startActivityIntentSender(caller, intent, fillInIntent, resolvedType,
                resultTo, resultWho, requestCode, flagsMask, flagsValues, options);
    }

    public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options)
            throws RemoteException {
        return m_orig.startNextMatchingActivity(callingActivity, intent, options);
    }

    public boolean finishActivity(IBinder token, int code, Intent data) throws RemoteException {
        return m_orig.finishActivity(token, code, data);
    }

    public void finishSubActivity(IBinder token, String resultWho, int requestCode)
            throws RemoteException {
        m_orig.finishSubActivity(token, resultWho, requestCode);
    }

    public boolean finishActivityAffinity(IBinder token) throws RemoteException {
        return m_orig.finishActivityAffinity(token);
    }

    public boolean willActivityBeVisible(IBinder token) throws RemoteException {
        return m_orig.willActivityBeVisible(token);
    }

    public Intent registerReceiver(IApplicationThread caller, String callerPackage,
            IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId)
            throws RemoteException {
        return m_orig.registerReceiver(caller, callerPackage, receiver, filter, requiredPermission,
                userId);
    }

    public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
        m_orig.unregisterReceiver(receiver);
    }

    public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType,
            IIntentReceiver resultTo, int resultCode, String resultData, Bundle map,
            String requiredPermission, boolean serialized, boolean sticky, int userId)
            throws RemoteException {
        return m_orig.broadcastIntent(caller, intent, resolvedType, resultTo, resultCode,
                resultData, map, requiredPermission, serialized, sticky, userId);
    }

    public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId)
            throws RemoteException {
        m_orig.unbroadcastIntent(caller, intent, userId);
    }

    public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map,
            boolean abortBroadcast) throws RemoteException {
        m_orig.finishReceiver(who, resultCode, resultData, map, abortBroadcast);
    }

    public void attachApplication(IApplicationThread app) throws RemoteException {
        m_orig.attachApplication(app);
    }

    public void activityResumed(IBinder token) throws RemoteException {
        m_orig.activityResumed(token);
    }

    public void activityIdle(IBinder token, Configuration config, boolean stopProfiling)
            throws RemoteException {
        m_orig.activityIdle(token, config, stopProfiling);
    }

    public void activityPaused(IBinder token) throws RemoteException {
        m_orig.activityPaused(token);
    }

    public void activityStopped(IBinder token, Bundle state, Bitmap thumbnail,
            CharSequence description) throws RemoteException {
        m_orig.activityStopped(token, state, thumbnail, description);
    }

    public void activitySlept(IBinder token) throws RemoteException {
        m_orig.activitySlept(token);
    }

    public void activityDestroyed(IBinder token) throws RemoteException {
        m_orig.activityDestroyed(token);
    }

    public String getCallingPackage(IBinder token) throws RemoteException {
        return m_orig.getCallingPackage(token);
    }

    public ComponentName getCallingActivity(IBinder token) throws RemoteException {
        return m_orig.getCallingActivity(token);
    }

    public List getTasks(int maxNum, int flags, IThumbnailReceiver receiver) throws RemoteException {
        return m_orig.getTasks(maxNum, flags, receiver);
    }

    public List<RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId)
            throws RemoteException {
        return m_orig.getRecentTasks(maxNum, flags, userId);
    }

    public TaskThumbnails getTaskThumbnails(int taskId) throws RemoteException {
        return m_orig.getTaskThumbnails(taskId);
    }

    public Bitmap getTaskTopThumbnail(int taskId) throws RemoteException {
        return m_orig.getTaskTopThumbnail(taskId);
    }

    public List getServices(int maxNum, int flags) throws RemoteException {
        return m_orig.getServices(maxNum, flags);
    }

    public List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
        return m_orig.getProcessesInErrorState();
    }

    public void moveTaskToFront(int task, int flags, Bundle options) throws RemoteException {
        m_orig.moveTaskToFront(task, flags, options);
    }

    public void moveTaskToBack(int task) throws RemoteException {
        m_orig.moveTaskToBack(task);
    }

    public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
        return m_orig.moveActivityTaskToBack(token, nonRoot);
    }

    public void moveTaskBackwards(int task) throws RemoteException {
        m_orig.moveTaskBackwards(task);
    }

    public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
        return m_orig.getTaskForActivity(token, onlyRoot);
    }

    public void reportThumbnail(IBinder token, Bitmap thumbnail, CharSequence description)
            throws RemoteException {
        m_orig.reportThumbnail(token, thumbnail, description);
    }

    public ContentProviderHolder getContentProvider(IApplicationThread caller, String name,
            int userId, boolean stable) throws RemoteException {
        return m_orig.getContentProvider(caller, name, userId, stable);
    }

    public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token)
            throws RemoteException {
        return m_orig.getContentProviderExternal(name, userId, token);
    }

    public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
        m_orig.removeContentProvider(connection, stable);
    }

    public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
        m_orig.removeContentProviderExternal(name, token);
    }

    public void publishContentProviders(IApplicationThread caller,
            List<ContentProviderHolder> providers) throws RemoteException {
        m_orig.publishContentProviders(caller, providers);
    }

    public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta)
            throws RemoteException {
        return m_orig.refContentProvider(connection, stableDelta, unstableDelta);
    }

    public void unstableProviderDied(IBinder connection) throws RemoteException {
        m_orig.unstableProviderDied(connection);
    }

    public PendingIntent getRunningServiceControlPanel(ComponentName service)
            throws RemoteException {
        return m_orig.getRunningServiceControlPanel(service);
    }

    public ComponentName startService(IApplicationThread caller, Intent service,
            String resolvedType, int userId) throws RemoteException {
        return m_orig.startService(caller, service, resolvedType, userId);
    }

    public int stopService(IApplicationThread caller, Intent service, String resolvedType,
            int userId) throws RemoteException {
        return m_orig.stopService(caller, service, resolvedType, userId);
    }

    public boolean stopServiceToken(ComponentName className, IBinder token, int startId)
            throws RemoteException {
        return m_orig.stopServiceToken(className, token, startId);
    }

    public void setServiceForeground(ComponentName className, IBinder token, int id,
            Notification notification, boolean keepNotification) throws RemoteException {
        m_orig.setServiceForeground(className, token, id, notification, keepNotification);
    }

    public int bindService(IApplicationThread caller, IBinder token, Intent service,
            String resolvedType, IServiceConnection connection, int flags, int userId)
            throws RemoteException {
        return m_orig.bindService(caller, token, service, resolvedType, connection, flags, userId);
    }

    public boolean unbindService(IServiceConnection connection) throws RemoteException {
        return m_orig.unbindService(connection);
    }

    public void publishService(IBinder token, Intent intent, IBinder service)
            throws RemoteException {
        m_orig.publishService(token, intent, service);
    }

    public void unbindFinished(IBinder token, Intent service, boolean doRebind)
            throws RemoteException {
        m_orig.unbindFinished(token, service, doRebind);
    }

    public void serviceDoneExecuting(IBinder token, int type, int startId, int res)
            throws RemoteException {
        m_orig.serviceDoneExecuting(token, type, startId, res);
    }

    public IBinder peekService(Intent service, String resolvedType) throws RemoteException {
        return m_orig.peekService(service, resolvedType);
    }

    public boolean bindBackupAgent(ApplicationInfo appInfo, int backupRestoreMode)
            throws RemoteException {
        return m_orig.bindBackupAgent(appInfo, backupRestoreMode);
    }

    public void clearPendingBackup() throws RemoteException {
        m_orig.clearPendingBackup();
    }

    public void backupAgentCreated(String packageName, IBinder agent) throws RemoteException {
        m_orig.backupAgentCreated(packageName, agent);
    }

    public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
        m_orig.unbindBackupAgent(appInfo);
    }

    public void killApplicationProcess(String processName, int uid) throws RemoteException {
        m_orig.killApplicationProcess(processName, uid);
    }

    public boolean startInstrumentation(ComponentName className, String profileFile, int flags,
            Bundle arguments, IInstrumentationWatcher watcher, int userId) throws RemoteException {
        return m_orig.startInstrumentation(className, profileFile, flags, arguments, watcher,
                userId);
    }

    public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results)
            throws RemoteException {
        m_orig.finishInstrumentation(target, resultCode, results);
    }

    public Configuration getConfiguration() throws RemoteException {
        return m_orig.getConfiguration();
    }

    public void updateConfiguration(Configuration values) throws RemoteException {
        m_orig.updateConfiguration(values);
    }

    public void setRequestedOrientation(IBinder token, int requestedOrientation)
            throws RemoteException {
        m_orig.setRequestedOrientation(token, requestedOrientation);
    }

    public int getRequestedOrientation(IBinder token) throws RemoteException {
        return m_orig.getRequestedOrientation(token);
    }

    public ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
        return m_orig.getActivityClassForToken(token);
    }

    public String getPackageForToken(IBinder token) throws RemoteException {
        return m_orig.getPackageForToken(token);
    }

    public IIntentSender getIntentSender(int type, String packageName, IBinder token,
            String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags,
            Bundle options, int userId) throws RemoteException {
        return m_orig.getIntentSender(type, packageName, token, resultWho, requestCode, intents,
                resolvedTypes, flags, options, userId);
    }

    public void cancelIntentSender(IIntentSender sender) throws RemoteException {
        m_orig.cancelIntentSender(sender);
    }

    public boolean clearApplicationUserData(String packageName, IPackageDataObserver observer,
            int userId) throws RemoteException {
        return m_orig.clearApplicationUserData(packageName, observer, userId);
    }

    public String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
        return m_orig.getPackageForIntentSender(sender);
    }

    public int getUidForIntentSender(IIntentSender sender) throws RemoteException {
        return m_orig.getUidForIntentSender(sender);
    }

    public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll,
            boolean requireFull, String name, String callerPackage) throws RemoteException {
        return m_orig.handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull,
                name, callerPackage);
    }

    public void setProcessLimit(int max) throws RemoteException {
        m_orig.setProcessLimit(max);
    }

    public int getProcessLimit() throws RemoteException {
        return m_orig.getProcessLimit();
    }

    public void setProcessForeground(IBinder token, int pid, boolean isForeground)
            throws RemoteException {
        m_orig.setProcessForeground(token, pid, isForeground);
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int mode) throws RemoteException {
        return m_orig.checkUriPermission(uri, pid, uid, mode);
    }

    public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode)
            throws RemoteException {
        m_orig.grantUriPermission(caller, targetPkg, uri, mode);
    }

    public void revokeUriPermission(IApplicationThread caller, Uri uri, int mode)
            throws RemoteException {
        m_orig.revokeUriPermission(caller, uri, mode);
    }

    public void showWaitingForDebugger(IApplicationThread who, boolean waiting)
            throws RemoteException {
        m_orig.showWaitingForDebugger(who, waiting);
    }

    public void getMemoryInfo(MemoryInfo outInfo) throws RemoteException {
        m_orig.getMemoryInfo(outInfo);
    }

    public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
        m_orig.killBackgroundProcesses(packageName, userId);
    }

    public void killAllBackgroundProcesses() throws RemoteException {
        m_orig.killAllBackgroundProcesses();
    }

    public void forceStopPackage(String packageName, int userId) throws RemoteException {
        m_orig.forceStopPackage(packageName, userId);
    }

    public void goingToSleep() throws RemoteException {
        m_orig.goingToSleep();
    }

    public void wakingUp() throws RemoteException {
        m_orig.wakingUp();
    }

    public void setLockScreenShown(boolean shown) throws RemoteException {
        m_orig.setLockScreenShown(shown);
    }

    public void unhandledBack() throws RemoteException {
        m_orig.unhandledBack();
    }

    public ParcelFileDescriptor openContentUri(Uri uri) throws RemoteException {
        return m_orig.openContentUri(uri);
    }

    public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent)
            throws RemoteException {
        m_orig.setDebugApp(packageName, waitForDebugger, persistent);
    }

    public void setAlwaysFinish(boolean enabled) throws RemoteException {
        m_orig.setAlwaysFinish(enabled);
    }

    public void setActivityController(IActivityController watcher) throws RemoteException {
        m_orig.setActivityController(watcher);
    }

    public void enterSafeMode() throws RemoteException {
        m_orig.enterSafeMode();
    }

    public void noteWakeupAlarm(IIntentSender sender) throws RemoteException {
        m_orig.noteWakeupAlarm(sender);
    }

    public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
        return m_orig.killPids(pids, reason, secure);
    }

    public boolean killProcessesBelowForeground(String reason) throws RemoteException {
        return m_orig.killProcessesBelowForeground(reason);
    }

    public void startRunning(String pkg, String cls, String action, String data)
            throws RemoteException {
        m_orig.startRunning(pkg, cls, action, data);
    }

    public void handleApplicationCrash(IBinder app, CrashInfo crashInfo) throws RemoteException {
        m_orig.handleApplicationCrash(app, crashInfo);
    }

    public boolean handleApplicationWtf(IBinder app, String tag, CrashInfo crashInfo)
            throws RemoteException {
        return m_orig.handleApplicationWtf(app, tag, crashInfo);
    }

    public void handleApplicationStrictModeViolation(IBinder app, int violationMask,
            ViolationInfo crashInfo) throws RemoteException {
        m_orig.handleApplicationStrictModeViolation(app, violationMask, crashInfo);
    }

    public void signalPersistentProcesses(int signal) throws RemoteException {
        m_orig.signalPersistentProcesses(signal);
    }

    public List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
        return m_orig.getRunningAppProcesses();
    }

    public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
        return m_orig.getRunningExternalApplications();
    }

    public void getMyMemoryState(RunningAppProcessInfo outInfo) throws RemoteException {
        m_orig.getMyMemoryState(outInfo);
    }

    public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
        return m_orig.getDeviceConfigurationInfo();
    }

    public boolean profileControl(String process, int userId, boolean start, String path,
            ParcelFileDescriptor fd, int profileType) throws RemoteException {
        return m_orig.profileControl(process, userId, start, path, fd, profileType);
    }

    public boolean shutdown(int timeout) throws RemoteException {
        return m_orig.shutdown(timeout);
    }

    public void stopAppSwitches() throws RemoteException {
        m_orig.stopAppSwitches();
    }

    public void resumeAppSwitches() throws RemoteException {
        m_orig.resumeAppSwitches();
    }

    public void killApplicationWithAppId(String pkg, int appid) throws RemoteException {
        m_orig.killApplicationWithAppId(pkg, appid);
    }

    public void closeSystemDialogs(String reason) throws RemoteException {
        m_orig.closeSystemDialogs(reason);
    }

    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
        return m_orig.getProcessMemoryInfo(pids);
    }

    public void overridePendingTransition(IBinder token, String packageName, int enterAnim,
            int exitAnim) throws RemoteException {
        m_orig.overridePendingTransition(token, packageName, enterAnim, exitAnim);
    }

    public boolean isUserAMonkey() throws RemoteException {
        return m_orig.isUserAMonkey();
    }

    public void finishHeavyWeightApp() throws RemoteException {
        m_orig.finishHeavyWeightApp();
    }

    public void setImmersive(IBinder token, boolean immersive) throws RemoteException {
        m_orig.setImmersive(token, immersive);
    }

    public boolean isImmersive(IBinder token) throws RemoteException {
        return m_orig.isImmersive(token);
    }

    public boolean isTopActivityImmersive() throws RemoteException {
        return m_orig.isTopActivityImmersive();
    }

    public void crashApplication(int uid, int initialPid, String packageName, String message)
            throws RemoteException {
        m_orig.crashApplication(uid, initialPid, packageName, message);
    }

    public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
        return m_orig.getProviderMimeType(uri, userId);
    }

    public IBinder newUriPermissionOwner(String name) throws RemoteException {
        return m_orig.newUriPermissionOwner(name);
    }

    public void grantUriPermissionFromOwner(IBinder owner, int fromUid, String targetPkg, Uri uri,
            int mode) throws RemoteException {
        m_orig.grantUriPermissionFromOwner(owner, fromUid, targetPkg, uri, mode);
    }

    public void revokeUriPermissionFromOwner(IBinder owner, Uri uri, int mode)
            throws RemoteException {
        m_orig.revokeUriPermissionFromOwner(owner, uri, mode);
    }

    public int checkGrantUriPermission(int callingUid, String targetPkg, Uri uri, int modeFlags)
            throws RemoteException {
        return m_orig.checkGrantUriPermission(callingUid, targetPkg, uri, modeFlags);
    }

    public boolean dumpHeap(String process, int userId, boolean managed, String path,
            ParcelFileDescriptor fd) throws RemoteException {
        return m_orig.dumpHeap(process, userId, managed, path, fd);
    }

    public int startActivities(IApplicationThread caller, Intent[] intents, String[] resolvedTypes,
            IBinder resultTo, Bundle options, int userId) throws RemoteException {
        return m_orig.startActivities(caller, intents, resolvedTypes, resultTo, options, userId);
    }

    public int getFrontActivityScreenCompatMode() throws RemoteException {
        return m_orig.getFrontActivityScreenCompatMode();
    }

    public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
        m_orig.setFrontActivityScreenCompatMode(mode);
    }

    public int getPackageScreenCompatMode(String packageName) throws RemoteException {
        return m_orig.getPackageScreenCompatMode(packageName);
    }

    public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        m_orig.setPackageScreenCompatMode(packageName, mode);
    }

    public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
        return m_orig.getPackageAskScreenCompat(packageName);
    }

    public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
        m_orig.setPackageAskScreenCompat(packageName, ask);
    }

    public boolean switchUser(int userid) throws RemoteException {
        return m_orig.switchUser(userid);
    }

    public int stopUser(int userid, IStopUserCallback callback) throws RemoteException {
        return m_orig.stopUser(userid, callback);
    }

    public UserInfo getCurrentUser() throws RemoteException {
        return m_orig.getCurrentUser();
    }

    public boolean isUserRunning(int userid, boolean orStopping) throws RemoteException {
        return m_orig.isUserRunning(userid, orStopping);
    }

    public int[] getRunningUserIds() throws RemoteException {
        return m_orig.getRunningUserIds();
    }

    public boolean removeSubTask(int taskId, int subTaskIndex) throws RemoteException {
        return m_orig.removeSubTask(taskId, subTaskIndex);
    }

    public boolean removeTask(int taskId, int flags) throws RemoteException {
        return m_orig.removeTask(taskId, flags);
    }

    public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
        m_orig.registerProcessObserver(observer);
    }

    public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
        m_orig.unregisterProcessObserver(observer);
    }

    public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
        return m_orig.isIntentSenderTargetedToPackage(sender);
    }

    public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
        return m_orig.isIntentSenderAnActivity(sender);
    }

    public void updatePersistentConfiguration(Configuration values) throws RemoteException {
        m_orig.updatePersistentConfiguration(values);
    }

    public long[] getProcessPss(int[] pids) throws RemoteException {
        return m_orig.getProcessPss(pids);
    }

    public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
        m_orig.showBootMessage(msg, always);
    }

    public void dismissKeyguardOnNextActivity() throws RemoteException {
        m_orig.dismissKeyguardOnNextActivity();
    }

    public boolean targetTaskAffinityMatchesActivity(IBinder token, String destAffinity)
            throws RemoteException {
        return m_orig.targetTaskAffinityMatchesActivity(token, destAffinity);
    }

    public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData)
            throws RemoteException {
        return m_orig.navigateUpTo(token, target, resultCode, resultData);
    }

    public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
        return m_orig.getLaunchedFromUid(activityToken);
    }

    public void registerUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        m_orig.registerUserSwitchObserver(observer);
    }

    public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        m_orig.unregisterUserSwitchObserver(observer);
    }

    public void requestBugReport() throws RemoteException {
        m_orig.requestBugReport();
    }

    public long inputDispatchingTimedOut(int pid, boolean aboveSystem) throws RemoteException {
        return m_orig.inputDispatchingTimedOut(pid, aboveSystem);
    }

    public boolean testIsSystemReady() {
        return m_orig.testIsSystemReady();
    }

	// only dispatch
	
}
