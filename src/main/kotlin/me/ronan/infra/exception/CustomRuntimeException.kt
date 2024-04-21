package me.ronan.infra.exception

import me.ronan.infra.exception.ErrorCode

class LoginException(
    val errorCode: ErrorCode
): RuntimeException()

class ExistEmailException(
    val errorCode: ErrorCode
): RuntimeException()

class AlreadyCompletedOrderException(
    val errorCode: ErrorCode
): RuntimeException()

class AlreadyCompletedPointException(
    val errorCode: ErrorCode
): RuntimeException()

class AlreadyNotPendingOrderException(
    val errorCode: ErrorCode
): RuntimeException()

class AlreadyNotPendingPointException(
    val errorCode: ErrorCode
): RuntimeException()

class NoExistEmailException(
    val errorCode: ErrorCode
): RuntimeException()

class NoSuchMemberException(
    val errorCode: ErrorCode
): RuntimeException()

class NoSuchOrderException(
    val errorCode: ErrorCode
): RuntimeException()

class NoSuchPointException(
    val errorCode: ErrorCode
): RuntimeException()

class NotEnoughPointException(
    val errorCode: ErrorCode
): RuntimeException()

class NotEnoughStockException(
    val errorCode: ErrorCode
): RuntimeException()

class TooMuchAddPointException(
    val errorCode: ErrorCode
): RuntimeException()