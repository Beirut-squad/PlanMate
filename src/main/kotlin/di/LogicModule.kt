package org.example.di

import org.example.logic.use_case.authentication.*
import org.example.logic.use_case.authentication.encryption.EncryptPassword
import org.example.logic.use_case.authentication.encryption.Encryptor
import org.example.logic.use_case.authentication.encryption.EncryptorMD5Impl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logicModule = module {
    singleOf(::GetCurrentLoggedInUserUseCase)
    singleOf(::LoginUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::RegisterMateUseCase)
    singleOf(::RegisterUserOrAdminUseCase)
    singleOf(::EncryptPassword)
    single<Encryptor> {
        EncryptorMD5Impl()
    }
}