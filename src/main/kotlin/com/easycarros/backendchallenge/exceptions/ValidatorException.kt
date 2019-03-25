package com.easycarros.backendchallenge.exceptions

class ValidatorException(val errors: HashSet<String>) : Exception()
