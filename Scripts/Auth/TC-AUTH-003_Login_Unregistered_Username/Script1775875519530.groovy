import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType

RequestObject req = new RestRequestObjectBuilder()
    .withRestUrl('https://dummyjson.com/auth/login')
    .withRestRequestMethod('POST')
    .withTextBodyContent('{"username": "useryangtidakada", "password": "randompass"}')
    .withHttpHeaders([
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
    ])
    .build()

def response = WS.sendRequest(req)

WS.verifyResponseStatusCode(response, 400)

def message = WS.getElementPropertyValue(response, 'message')
assert message != null : "Error message harus ada!"

println "TC-AUTH-003 PASSED — Error message: ${message}"