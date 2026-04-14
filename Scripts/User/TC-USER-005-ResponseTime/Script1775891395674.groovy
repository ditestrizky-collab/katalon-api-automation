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
    .withRestUrl('https://dummyjson.com/auth/me')
    .withRestRequestMethod('GET')
    .withHttpHeaders([
        new TestObjectProperty('Authorization', ConditionType.EQUALS, "Bearer ${token}"),
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
    ])
    .build()

def response = WS.sendRequest(userReq)

WS.verifyResponseStatusCode(response, 200)

def elapsedTime = response.getElapsedTime()
assert elapsedTime < 1000 : "Response too slow! Actual: ${elapsedTime}ms, Threshold: 1000ms"

println "TC-USER-005 PASSED — Response time: ${elapsedTime}ms (threshold: 1000ms)"