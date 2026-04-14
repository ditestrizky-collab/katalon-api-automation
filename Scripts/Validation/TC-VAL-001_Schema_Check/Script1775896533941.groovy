import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import groovy.json.JsonSlurper


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


def json = new JsonSlurper().parseText(response.getResponseBodyContent())

def requiredKeys = ['id', 'firstName', 'lastName', 'email', 'username', 'role', 'image']
requiredKeys.each { key ->
    assert json.containsKey(key) : "Missing required field: ${key}"
}

println "TC-VAL-001 PASSED — All required schema fields present"