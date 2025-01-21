import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengetracker.R
import com.example.challengetracker.database.AppDatabase
import com.example.challengetracker.entity.Challenge
import com.example.challengetracker.repository.ChallengeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.hash.HashCode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID


class ChallengeViewModel(application: Application) : AndroidViewModel(application) {



    // Room setup
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val challengeRepository = ChallengeRepository(database.challengeDao())

    // State variables
    private val _loggedInUserId = MutableStateFlow<Int?>(null)
    val loggedInUserId: MutableStateFlow<Int?> get() = _loggedInUserId

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges: StateFlow<List<Challenge>> get() = _challenges

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> get() = _authError

    private val _defaultChallenges = MutableLiveData<List<Challenge>>()
    val defaultChallenges: LiveData<List<Challenge>> get() = _defaultChallenges

    private val _combinedChallenges = MutableStateFlow<List<Challenge>>(emptyList())
    val combinedChallenges: StateFlow<List<Challenge>> get() = _combinedChallenges

    private var isDefaultChallengesLoaded = false // Prevents duplicate loading

    init {
        observeChallenges()
        loadDefaultChallenges()
    }
    private fun observeChallenges() {
        // Collect from _challenges (StateFlow)
        viewModelScope.launch {
            _challenges.collect { challengesList ->
                combineChallenges(challengesList, _defaultChallenges.value)
            }
        }

        // Observe from _defaultChallenges (LiveData)
        _defaultChallenges.observeForever { defaultList ->
            combineChallenges(_challenges.value, defaultList)
        }
    }

    private fun combineChallenges(challengesList: List<Challenge>, defaultList: List<Challenge>?) {
        val combined = challengesList + (defaultList ?: emptyList())
        _combinedChallenges.value = combined
    }

    // Example method to update challenges
    fun setChallenges(newChallenges: List<Challenge>) {
        _challenges.value = newChallenges
    }

    // Example method to set default challenges
    fun setDefaultChallenges(newDefaultChallenges: List<Challenge>) {
        _defaultChallenges.value = newDefaultChallenges
    }

    fun loadDefaultChallenges() {
        if (isDefaultChallengesLoaded) return // Prevent multiple loads
        isDefaultChallengesLoaded = true

        viewModelScope.launch {
            try {
                val challenges = challengeRepository.getDefaultChallenges()
                _defaultChallenges.postValue(challenges)
                println("Loaded challenges: ${challenges.size}")
            } catch (e: Exception) {
                println("Error loading default challenges: ${e.localizedMessage}")
            }
        }
    }

    fun signUpUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Firebase user sign-up
                val result = firebaseAuth.createUserWithEmailAndPassword(username, password).await()
                val firebaseUser = result.user

                // Proceed with storing additional user details (if needed) in Firestore or local DB
                firebaseUser?.let {
                    // Save additional user details here, e.g., username, if necessary
                    _loggedInUserId.value = firebaseUser.uid.hashCode() // Storing user ID temporarily for simplicity
                    onResult(true)
                } ?: onResult(false)
            } catch (e: Exception) {
                _authError.value = e.localizedMessage
                onResult(false)
            }
        }
    }
    // Set the logged-in user ID
    fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    val uid = user?.uid // Get the unique user ID
                    _loggedInUserId.value=uid.hashCode()
                    println("Login successful. User ID: $uid")

                    if (uid != null) {
                        loadChallenges(uid)
                    }
                } else {
                    // Login failed
                    setAuthError("Error: ${task.exception?.message}")
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // User does not exist
                        setAuthError("Please sign in to create an account")
                    }
                    println("Error: ${task.exception?.message}")
                }
            }
    }

    fun logoutUser() {
        _loggedInUserId.value = null
        _challenges.value = emptyList()
    }

    // Load challenges for a specific user
    private fun loadChallenges(userId: String) {
        viewModelScope.launch {
            val userChallenges = userId?.let {
                challengeRepository.getChallengesByUserId(it)
            } ?: emptyList()
            _challenges.value = userChallenges
        }
    }

    // Add a new challenge only if a user is logged in
    fun addChallenge(title: String, description: String, duration: Int, type: String) {
        val userId = _loggedInUserId.value
        if (userId == null) {
            _authError.value = "User must be logged in to add a challenge"
            return
        }

        viewModelScope.launch {
            val newChallenge = Challenge(
                userId = userId,
                title = title,
                description = description,
                duration = duration,
                progress = 0,
             type = type
            )
            challengeRepository.insertChallenge(newChallenge)
            loadChallenges(userId.toString()) // Refresh the list
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }

    fun deleteChallenge(challengeId: Int) {
        viewModelScope.launch {
            if (challengeId != null) {
                challengeRepository.deleteChallengeById(challengeId)
                println("Challenge deleted: $challengeId")
               // loadChallenges(_loggedInUserId.value.toString())
                println(_combinedChallenges.value)
                _combinedChallenges.value = _combinedChallenges.value.filter { it.challengeId != challengeId }
                println(_combinedChallenges.value)

                // Refresh the challenge list
            } else {
                println("Challenge not found: $challengeId")
            }
        }
    }

    fun getTypeImage(type: String): Int {
        return when (type) {
            "Health" -> R.drawable.health
            "Fitness" -> R.drawable.img1
            "Learning" -> R.drawable.learn
            "Meditation" -> R.drawable.img12
            "Productivity" -> R.drawable.img7
            "Creative" -> R.drawable.img10

            else -> R.drawable.img6
        }
    }



    fun setAuthError(message: String?) {
        _authError.value = message ?: ""
        println("Auth Error Set: $message")
    }
    fun checkUsernameAndSignUp(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Try to create the user with email and password using Firebase
                val result = firebaseAuth.createUserWithEmailAndPassword(username, password).await()

                val firebaseUser = result.user

                firebaseUser?.let {
                    // If successful, store the user ID and proceed with additional logic
                    _loggedInUserId.value = firebaseUser.uid.hashCode()
                    onResult(true)
                } ?: onResult(false)

            } catch (e: FirebaseAuthUserCollisionException) {
                // If the email (username) is already in use
                _authError.value = "Username is already taken"
                onResult(false)
            } catch (e: Exception) {
                // Handle other errors
                _authError.value = e.localizedMessage
                onResult(false)
            }
        }
    }
    fun updateChallengeProgress(challengeId: Int, newProgress: Int) {
        viewModelScope.launch {
            val challenge = _combinedChallenges.value.find { it.challengeId == challengeId }
            if (challenge != null) {
                if(newProgress==challenge.duration){
                    challenge.isCompleted=true
                }
                val updatedChallenge = challenge.copy(progress = newProgress)
                challengeRepository.updateChallenge(updatedChallenge)
                // Directly update the list to reflect the progress change
                _combinedChallenges.value = _combinedChallenges.value.map {
                    if (it.challengeId == challengeId) updatedChallenge else it
                }
            }
        }
    }


}

