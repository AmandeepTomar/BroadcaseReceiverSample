## Lets Discuss About the broadcast Receiver

- Android Apps can send and receive the messages and events  from the android systen and other apps. Like Publis-subscribe patterns.
- Therefore, delivery times of broadcasts are not guaranteed
- Apps need to register the receiver to receive the events.
- **Whenever possible, keep broadcasts private to your app.**
- The system automatically sends broadcasts when various system events occur, such as when the system switches in and out of airplane mode. System broadcasts are sent to all apps that are subscribed to receive the event.
### Changes to system broadcasts
#### Android 14
- When app is in cached state the less important brodcast are deferred suct as  `ACTION_SCREEN_ON`. once the app comes is in active state the all deferred broadcast ready to delivered.

#### Android 9
- The` NETWORK_STATE_CHANGED_ACTION` broadcast doesn't receive information about the user's location or personally identifiable data.

#### Android 8
- Custom broadcast need to be regostered dynamically. in code.

#### Android 7.0

Android 7.0 (API level 24) and higher don't send the following system broadcasts
`ACTION_NEW_PICTURE
ACTION_NEW_VIDEO`
**Also, apps targeting Android 7.0 and higher must register the CONNECTIVITY_ACTION broadcast using registerReceiver(BroadcastReceiver, IntentFilter). Declaring a receiver in the manifest doesn't work.**

### Receiving broadcasts

Apps can receive broadcasts in two ways: through **manifest-declared** receivers and **context-registered** receivers.
- **Context-registered** they receive the broadcast as long as their registering content is valid. like if they registered using Activity context they will receive the broadcast untill the activity not destroyed. if they registerd using applicationContext than will receive broadcast throughout the application.
- `exported` it is define that other apps can access your broadcast or not so set it to false if you want only your app receive the broadcast.
```
val listenToBroadcastsFromOtherApps =  false  
val receiverFlags =  if  (listenToBroadcastsFromOtherApps)  {
  ContextCompat.RECEIVER_EXPORTED  
}  else  {  ContextCompat.RECEIVER_NOT_EXPORTED  }
```

Implementing Broadcast
```
class MyBroadcastReceiver : BroadcastReceiver() {  
override fun onReceive(context: Context?, intent: Intent?) {  
intent?.apply {  }  
}  }

// declare in manifest 
receiver  android:name=".MyBroadcastReceiver"  android:exported="false">  <intent-filter>  <action  android:name="APP_SPECIFIC_BROADCAST"  />  </intent-filter>  
</receiver>
// register in Activity 
val br:  BroadcastReceiver  =  MyBroadcastReceiver()

onStart(){
val filter =  IntentFilter(APP_SPECIFIC_BROADCAST)val listenToBroadcastsFromOtherApps =  false  
val receiverFlags =  if  (listenToBroadcastsFromOtherApps)  {  ContextCompat.RECEIVER_EXPORTED  
}  else  {  
ContextCompat.RECEIVER_NOT_EXPORTED  
}

ContextCompat.registerReceiver(context, br, filter, receiverFlags)
}

onStop(){
unregisterReceiver(android.content.BroadcastReceiver)
}
```

- The BroadcastReceiver is deactivated after `onReceive()`
- Thus, broadcast receivers should not initiate long-running background threads.
- The system can stop the process at any moment after `onReceive()` to reclaim memory, terminating the created thread.
- schedule a JobService from the receiver using the JobScheduler so the system knows the process is still working.

### Sending Broadcast.

- **sendOrderedBroadcast(Intent, String)**
  - method sends broadcasts to one receiver at a time.
  - As each receiver executes in turn, it can propagate a result to the next receiver
  - it can completely abort the broadcast so that it won't be passed to other receivers.
  - the order of execution is define by `priority` , if the `priority` is same than it execute in arbitrary order.

- **sendBroadcst(intent)**
  - sends broadcasts to all receivers in an undefined order
  - This is more efficient, but means that receivers cannot read results from other receivers

```
Intent().also { intent -> 
intent.setAction("com.example.broadcast.MY_NOTIFICATION") 
intent.putExtra("data",  "Nothing to see here, move along.") 
sendBroadcast(intent)  
}
```
### Restricting broadcasts with permissions
-  you can specify a permission parameter. Only receivers who have requested that permission with the tag in their manifest (and subsequently been granted the permission if it is dangerous) can receive the broadcast
```
sendBroadcast(Intent(BluetoothDevice.ACTION_FOUND),  Manifest.permission.BLUETOOTH_CONNECT)

<uses-permission android:name="android.permission.BLUETOOTH_CONNECT"/>

// Receiving With permission 


<uses-permission android:name="android.permission.BLUETOOTH_CONNECT"/>

<receiver android:name=".MyBroadcastReceiver"
          android:permission="android.permission.BLUETOOTH_CONNECT"> 
     <intent-filter> 
        <action android:name="android.intent.action.ACTION_FOUND"/>
    </intent-filter>
</receiver>

var filter =  IntentFilter(Intent.ACTION_FOUND)  
registerReceiver(receiver, filter,  Manifest.permission.BLUETOOTH_CONNECT,  null  )

```

#### Best Practices
- use the LocalBroadcastManager to sendBroadcast(). in locally inside the app only.
- If many apps have registered to receive the same broadcast in their manifest, it can cause the system to launch a lot of apps, causing a substantial impact on both device performance and user experience. To avoid this, prefer using context registration over manifest declaration
- Do not broadcast sensitive information using an implicit intent
- There are three ways to control who can receive your broadcasts:
  - You can specify a permission when sending a broadcast
  - setPackage(), so that only list of package will receive the broadcast.
  - eported = false.
- Do not start activities from broadcast receivers because the user experience is jarring; especially if there is more than one receiver
- receiver's onReceive(Context, Intent) method runs on the main thread,

#### Implicit broadcast exceptions
As part of the Android 8.0 (API level 26) background execution limits, apps that target the API level 26 or higher can't register broadcast receivers for implicit broadcasts in their manifest
- ACTION_LOCKED_BOOT_COMPLETED, ACTION_BOOT_COMPLETED
- ACTION_USER_INITIALIZE, android.intent.action.USER_ADDED, android.intent.action.USER_REMOVED
- android.intent.action.TIME_SET, ACTION_TIMEZONE_CHANGED, ACTION_NEXT_ALARM_CLOCK_CHANGED

and so on.

Q if we not defined receiver in amnifest and write to register the code using context register in Activity then what happen?
- If the receiver is successfully registered then it will receive the events or broadcast.

#

### Content Provider
- Manage access to data stored by itself or stored by other apps and provide a way to share data with other apps.
- you can configure a content provider to let other applications securely access and modify your app data
- The Android framework includes content providers that manage data such as `audio, video, images, and personal contact information`.
- `CursorLoader` to load data in the background
#### When to use the content providers
-  To access an existing content provider in another application
- Creating a new content provider in your application to share data with other applications
- Returning custom search suggestions for your application through the search framework using `SearchRecentSuggestionsProvider`
- Synchronizing application data with your server using an implementation of `AbstractThreadedSyncAdapter`
- Loading data in your UI using a `CursorLoader`

#### ContentResolver
- access data in a content provider
- UI<->ContentLoader<->ContentResolver<->ContentProvider<->DataStore.
- Youe Application has to request specific permission to use the content provider in manifext.
```
// Queries the UserDictionary and returns resultscursor = contentResolver.query(  
UserDictionary.Words.CONTENT_URI,  	// The content URI of the words table 
projection,  						// The columns to return for each row
selectionClause,  					// Selection criteria 
selectionArgs.toTypedArray(),  		// Selection criteria
sortOrder 							// The sort order for the returned rows  
)
```
- It return the `cursor` as we iterate the data on the main thread or UI thread. We need to process the cursor or data on the background thread. for that we can use threading or Here `Loaders` comes , that by default load the content on the backgroud UI.
-  Advantages of using loader is , its provide the latest/update data always as anychages happen in the third party app it will update automatically.

#### Content URIs
- URI that identifies data in a provider
- `content://user_dictionary/words`

  -   The  `content://`  string is the  _scheme_, which is always present and identifies this as a content URI.
  -   The  `user_dictionary`  string is the provider's authority.
  -   The  `words`  string is the table's path.
#### Retrieve data from the provider
1.  Request read access permission for the provider.
2.  sends a query to the provider.
- Example of permissions
- The User Dictionary Provider requires the `android.permission.READ_USER_DICTIONARY` permission to retrieve data from it. The provider has a separate `android.permission.WRITE_USER_DICTIONARY` permission for inserting, updating, or deleting data.
- By default, all applications can read from or write to your provider, even if the underlying data is private, because by default your provider doesn't have permissions set.

-  Insert,  update,  and delete data
#### The <provider> element
- `Authority (android:authorities)`
- Provider class name `(android:name)`
- Permissions
  - `android:grantUriPermissions: `temporary permission flag
  - `android:permission:` single provider-wide read/write permission
  - `android:readPermission`: provider-wide read permission
  - `android:writePermission:` provider-wide write permission
- Storage Provider : - just like file system apps in android.

