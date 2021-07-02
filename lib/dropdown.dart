import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:async';
import 'dart:convert' show json;

class Dropdown extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _DropdownState();
}

class _DropdownState extends State<Dropdown> {
//  List<String> _locations = ['Please choose a location', 'A', 'B', 'C', 'D']; // Option 1
//  String _selectedLocation = 'Please choose a location'; // Option 1
  List<String> _locations = new List(); // Option 2;

  String _selectedLocation; // Option 2
  Future<String> postData() async {
    http.Response res = await http.post(
        'http://10.0.0.142:8006/shravan-cgi/GetConfigurationData.cgi'); // post api call
    print(res.body.toString());
    String str = res.body.toString();
    str.replaceAll('\n', ";");
    Map data = json.decode(res.body);

    List<String> list = new List();
    setState(() {
      for (int i = 0; i < data.length; i++) {
        _locations.add(data[i].toString().replaceAll('\n', ''));
      }
      //   _locations.add("shravan");
    });
    print(list.toString());
    return "success";
  }

  @override
  void initState() {
    super.initState();
    // this should not be done in build method.
    this.postData();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Drop down'),
        ),
        body: Container(
          margin: EdgeInsets.all(10),
          padding: EdgeInsets.all(5),
          decoration: BoxDecoration(
              color: Colors.cyan,
              border: Border.all(color: Colors.black),
              borderRadius: BorderRadius.circular(10)),
          child: DropdownButton(
            isExpanded: false,
            dropdownColor: Colors.lightBlue[100],
            focusColor: Colors.black,

            hint: Text(
              'Please choose a location',
              style: TextStyle(
                color: Colors.black,
              ),
            ),
            // Not necessary for Option 1
            value: _selectedLocation,
            onChanged: (newValue) {
              setState(() {
                _selectedLocation = newValue;
                print("hello" + _locations.toString());
              });
            },
            items: _locations.map((location) {
              return DropdownMenuItem(
                child: new Text(location),
                value: location,
              );
            }).toList(),
          ),
        ),
      ),
    );
  }
}
