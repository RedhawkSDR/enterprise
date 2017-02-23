## Camel REDHAWK Example
***

This example application shows how you can use Camel REDHAWK component to subscribe to an Event Channel and publish that data to a JMS Topic. 

### Set Up
***

In order to run this demo you need to setup the example component that will publish data to a REDHAWK Event Channel and the Waveform to run that component must be launched. Each of the project described below can be imported into REDHAWK using the 'REDHAWK Import Wizard' provided by the IDE. 

* EventSpitter: Sends out a struct every second. 
* SpitToChannel: Uses the EventSpitter component to send data to an EventChannel called EventsSpat

Once you've launched the SpitToChannel Waveform and ensure that it's been started. You can run the following command to see the event messages from your component:

    eventviewer REDHAWK_DEV EventsSpat
    

### Example Output

	eventviewer REDHAWK_DEV EventsSpat
	Receiving events. Press 'enter' key to exit
	[{'id': 'properties', 'value': [{'id': 'foo', 'value': 'foo-04edebfc-6a8b-4f56-b018-25bbc46a842a'}, {'id': 'bar', 'value': 1.6000000238418579}]}]
	[{'id': 'properties', 'value': [{'id': 'foo', 'value': 'foo-09df6252-15fc-4af8-8e6e-5c6db2dbe3b5'}, {'id': 'bar', 'value': 1.6000000238418579}]}]
	[{'id': 'properties', 'value': [{'id': 'foo', 'value': 'foo-0c34db0e-4dae-4315-a344-73d50b8d2a8f'}, {'id': 'bar', 'value': 1.6000000238418579}]}]
	[{'id': 'properties', 'value': [{'id': 'foo', 'value': 'foo-38d514f1-29bc-4939-8b34-0e8e7246c0cb'}, {'id': 'bar', 'value': 1.6000000238418579}]}]
	[{'id': 'properties', 'value': [{'id': 'foo', 'value': 'foo-5cb7f365-1488-4130-8917-8b9b3f03430e'}, {'id': 'bar', 'value': 1.6000000238418579}]}]
	[{'id': 'properties', 'value': [{'id': 'foo', 'value': 'foo-a5ecffae-39dc-43a9-80ee-f3902dcd0929'}, {'id': 'bar', 'value': 1.6000000238418579}]}]


### Instructions
