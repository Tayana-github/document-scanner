
import 'package:flutter/material.dart';

import 'package:shared_preferences/shared_preferences.dart';

/// This is the stateful widget that the main application instantiates.
class SilverViewwidget extends StatefulWidget {
  SilverViewwidget({Key key}) : super(key: key);

  @override
  _SilverViewwidgetState createState() => _SilverViewwidgetState();
}

/// This is the private State class that goes with MyStatefulWidget.
class _SilverViewwidgetState extends State<SilverViewwidget> {
  int _count = 0;


  @override
  void initState() {
    super.initState();
    // this should not be done in build method.

  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(

        body: CustomScrollView(
          slivers: <Widget>[
            SliverAppBar(
              title: Text('SliverAppBar'),
              backgroundColor: Colors.green,
              expandedHeight: 200.0,
              flexibleSpace: FlexibleSpaceBar(
                background: Image.asset('assets/forest.jpg', fit: BoxFit.cover),
              ),
            ),
            SliverFixedExtentList(
              itemExtent: 150.0,
              delegate: SliverChildListDelegate(
                [
                  Container(color: Colors.red),
                  Container(color: Colors.purple),
                  Container(color: Colors.green),
                  Container(color: Colors.orange),
                  Container(color: Colors.yellow),
                  Container(color: Colors.pink),
                ],
              ),
            ),
          ],
        ));
  }
}