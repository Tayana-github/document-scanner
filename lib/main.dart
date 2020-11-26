import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:simpleflutter_app/silverview.dart';
import 'package:simpleflutter_app/twitterlogin.dart';
import "grid_view.dart";
import "list_view.dart";
import "scaffold_widget.dart";
import "login.dart";
import "dropdown.dart";
import 'manageAccounts.dart';
import 'streambuilder.dart';
import 'package:shared_preferences/shared_preferences.dart';
void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}


class MyApp extends StatelessWidget {
  final appTitle = 'Simple App';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: new ThemeData(
        accentColor: Colors.blue,
        primarySwatch: Colors.blue,
        brightness: Brightness.light,
      ),
      title: appTitle,
      home: MyHomePage(title: appTitle),
    );
  }
}
class MyHomePage extends StatefulWidget {


  final String title;
  MyHomePage({Key key, this.title}) : super(key: key);

  @override
  _MyHomePageWidgetState createState() => _MyHomePageWidgetState();
}
class _MyHomePageWidgetState extends State<MyHomePage> {
  bool darkTheme = false;
  _MyHomePageWidgetState({this.darkTheme});
  @override
  Widget build(BuildContext context) {
    return Scaffold(

      appBar: AppBar(title: Text(widget.title)),
      body: Center(child: Text('My Page!')),
      drawer: Drawer(
        // Add a ListView to the drawer. This ensures the user can scroll
        // through the options in the drawer if there isn't enough vertical
        // space to fit everything.
        child: ListView(
          // Important: Remove any padding from the ListView.
          padding: EdgeInsets.zero,
          children: <Widget>[
            SizedBox(

          height: 100.0,
            child: DrawerHeader(
              padding:EdgeInsets.all(20.0),

              child: Text('Bill management',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20,color: Colors.yellow),

                  ),
              decoration: BoxDecoration(
                color: Colors.redAccent,

              ),
            ),
            ),
            ListTile(
              leading:  Icon(
                Icons.home,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('Scaffold'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => ScaffoldWidget()));
              },
            ),
            ListTile(
              leading:  Icon(
                Icons.list,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('ListView'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => ListScreen()));
              },
            ),
            ListTile(
              leading:  Icon(
                Icons.list,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('GridView'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => GridScreen()));
              },
            ),
            ListTile(
              leading:  Icon(
                Icons.list,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('Drop down'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => Dropdown()));
              },
            ),

            ListTile(
              leading:  Icon(
                Icons.login,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('Login'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => Login()));


              },
            ),
            ListTile(
              leading:  Icon(
                Icons.login,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('Manage accounts'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => WebViewExample()));


              },

            ),
            ListTile(
              leading:  Icon(
                Icons.home,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('Scaffold'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => HomePage()));
              },
            ),
            ListTile(
              leading:  Icon(
                Icons.home,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('SilverView'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.of(context).push(MaterialPageRoute(builder: (context) => SilverViewwidget()));
              },

            ),
            ListTile(
              leading:  Icon(
                Icons.home,
                color: Colors.pink,
                size: 24.0,
                semanticLabel: 'Text to announce in accessibility modes',
              ),
              title: Text('Twitter'),
              onTap: () {

                Navigator.of(context).push(MaterialPageRoute(builder: (context) => Twitter()));
              },

            ),
            SwitchListTile(
              title: Text('Dark Mode'),
              value: true,
              onChanged: (bool value) {
                setState(() {
                  darkTheme = value;
                });
              },
            ),
          ],
        ),
      ),
    );
  }
}

