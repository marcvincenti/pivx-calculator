import os
import boto3
import urllib2
import json
import re

dynamodb = boto3.resource('dynamodb')
url = 'http://178.254.23.111/~pub/DN/DN_masternode_payments_stats.html'

def masternodes(event, context):
	
	table = dynamodb.Table(os.environ['DYNAMODB_TABLE'])

	content = urllib2.urlopen(url).read().split('\n')
	pattern=re.compile(r'<b[^>]*>([^<]+)</b>')

	table.update_item(
		Key={
			'coin': 'pivx'
		},
		UpdateExpression="set masternodes_count = :m, available_supply = :s ",
		ExpressionAttributeValues={
			':m': re.findall(pattern, content[27]).pop().replace(" ", ""),
			':s': re.findall(pattern, content[36]).pop().replace(" ", ""),
		},
		ReturnValues="UPDATED_NEW"
	)

