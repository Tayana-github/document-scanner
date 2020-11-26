
import 'package:flutter/material.dart';

import 'package:shared_preferences/shared_preferences.dart';

/// This is the stateful widget that the main application instantiates.
class ScaffoldWidget extends StatefulWidget {
  ScaffoldWidget({Key key}) : super(key: key);

  @override
  _ScaffoldWidgetState createState() => _ScaffoldWidgetState();
}

/// This is the private State class that goes with MyStatefulWidget.
class _ScaffoldWidgetState extends State<ScaffoldWidget> {
  int _count = 0;

  _counter() async {
    print("hi shravan");
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int counter = (prefs.getInt('counter') ?? 0);
    print('Pressed $counter times.');
    setState(() {
      _count = counter;
    });
    await prefs.setInt('counter', counter);
  }

  @override
  void initState() {
    super.initState();
    // this should not be done in build method.
    this._counter();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Scaffold"),
      ),
      body: new Container(),

      /// Builders let you pass context
      /// from your *current* build method
      /// Directly to children returned in this build method
      ///
      /// The 'builder' property accepts a callback
      /// which can be treated exactly as a 'build' method on any
      /// widget
      floatingActionButton: new Builder(builder: (BuildContext context) {
        return new FloatingActionButton(onPressed: () {
          Scaffold.of(context).showSnackBar(
            new SnackBar(
              backgroundColor: Colors.blue,
              content: new Text('SnackBar'),
            ),
          );
        });
      }),
    );
  }
}