import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:url_launcher/url_launcher.dart';

//import 'package:flutter_inappwebview/flutter_inappwebview.dart';
class WebViewExample extends StatefulWidget {
  @override
  WebViewExampleState createState() => WebViewExampleState();
}

class WebViewExampleState extends State<WebViewExample> {
  @override
  void initState() {
    super.initState();
    // Enable hybrid composition.
    if (Platform.isAndroid) WebView.platform = SurfaceAndroidWebView();
  }

  @override
  Widget build(BuildContext context) {
    return WebView(
      initialUrl: 'https://online.anyflip.com/guyy/baaz/mobile/index.html',
      javascriptMode: JavascriptMode.unrestricted,
      initialMediaPlaybackPolicy: AutoMediaPlaybackPolicy.always_allow,
      gestureNavigationEnabled: true,
      onPageStarted: (url) {
        print("started");
      },
      onPageFinished: (url) {
        print("finished");
      },
    );
  }
}