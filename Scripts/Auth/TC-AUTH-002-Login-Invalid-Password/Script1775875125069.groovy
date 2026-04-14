import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType

RequestObject req = new RestRequestObjectBuilder()
    .withRestUrl('https://dummyjson.com/auth/login')
    .withRestRequestMethod('POST')
    .withTextBodyContent('{"username": "emilys", "password": "wrongpassword"}')
    .withHttpHeaders([
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
    ])
    .build()

def response = WS.sendRequest(req)

WS.verifyResponseStatusCode(response, 400)

def message = WS.getElementPropertyValue(response, 'message')
assert message == 'Invalid credentials' : "Wrong error message!"

println "✅ TC-AUTH-002 PASSED — Error message: ${message}"