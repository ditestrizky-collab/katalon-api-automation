import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType

//Get token

RequestObject loginRequest = new RestRequestObjectBuilder()
.withRestUrl('https://dummyjson.com/auth/login')
.withRestRequestMethod('POST')
.withTextBodyContent('{"username": "emilys", "password": "emilyspass"}')
.withHttpHeaders([
	new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
])
.build()

def loginResponse = WS.sendRequest(loginRequest)
WS.verifyResponseStatusCode(loginResponse, 200)

def token = WS.getElementPropertyValue(loginResponse, 'accessToken')
assert token != null && token !='' : "Token tidak boleh kosong!"

RequestObject userRequest = new RestRequestObjectBuilder()
.withRestUrl('https://dummyjson.com/auth/me')
.withRestRequestMethod('GET')
.withHttpHeaders([
	new TestObjectProperty('Authorization', ConditionType.EQUALS, "Bearer ${token}"),
	new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
])
.build()

def userResponse = WS.sendRequest(userRequest)

//println "Status:${userResponse.getStatusCode()}"
//println "Body:${userResponse.getResponseBodyContent()}"

WS.verifyResponseStatusCode(userResponse, 200)
assert userResponse.getElapsedTime() < 2000 :"Responsenya Cukup Memakan Waktu"

def id = WS.getElementPropertyValue(userResponse,'id')
def firstName = WS.getElementPropertyValue(userResponse,'firstName')
def lastName = WS.getElementPropertyValue(userResponse,'lastName')
def email = WS.getElementPropertyValue(userResponse, 'email')
def username = WS.getElementPropertyValue(userResponse, 'username')
def role = WS.getElementPropertyValue(userResponse,'role')

assert id != null : "id harus ada!"
assert firstName != null : "firstName harus ada!"
assert lastName != null : "lastName harus ada!"
assert email != null : "email harus ada!"
assert username == 'emilys' : "Username should be emilys!"
assert role != null : "role harus ada!"

assert id instanceof Integer : "id harus int"
assert firstName instanceof String : "firstName harus string yaa"

println "TC-USER-001 PASSED - User: ${firstName} ${lastName}, email: ${email}, role: ${role}"