import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType


RequestObject loginReq = new RestRequestObjectBuilder()
    .withRestUrl('https://dummyjson.com/auth/login')
    .withRestRequestMethod('POST')
    .withTextBodyContent('{"username": "emilys", "password": "emilyspass"}')
    .withHttpHeaders([
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
    ])
    .build()

def loginResponse = WS.sendRequest(loginReq)
def token = WS.getElementPropertyValue(loginResponse, 'accessToken')

RequestObject userReq = new RestRequestObjectBuilder()
    .withRestUrl('https://dummyjson.com/users/3124')
    .withRestRequestMethod('GET')
    .withHttpHeaders([
        new TestObjectProperty('Authorization', ConditionType.EQUALS, "Bearer ${token}"),
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
    ])
    .build()

def response = WS.sendRequest(userReq)

WS.verifyResponseStatusCode(response, 404)

def message = WS.getElementPropertyValue(response, 'message')
assert message != null : "Error message should exist!"

println "TC-USER-004 PASSED — Non-existent user correctly returned 404: ${message}"