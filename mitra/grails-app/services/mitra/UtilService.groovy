package mitra

class UtilService {

    def getExistingUserFromParams(deviceId) {
        if (!deviceId) {
            return null
        }

        return User.findByDeviceId(deviceId)
    }

    def getUser(String deviceId) {
        def existingUser = getExistingUserFromParams(deviceId)
        if (existingUser) {
            return existingUser
        }
        User user = new User(deviceId:deviceId)
        user.save(flush: true)
    }

}
