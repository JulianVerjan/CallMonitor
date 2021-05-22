package com.wallet.test.data.repositories

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wallet.data.datasource.CurrencyWalletRemoteDataSource
import com.wallet.data.repository.CurrencyWalletRepository
import com.wallet.network.model.NetworkResponse
import com.wallet.network.model.mapper.RepositoryResult
import com.wallet.network.model.reponse.CurrencyNetworkResponse
import com.wallet.network.model.reponse.ServiceNetworkResponse
import com.wallet.network.model.reponse.WalletNetworkResponse
import com.wallet.network.service.CurrencyWalletService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WalletsRepositoryTest {

    @MockK(relaxed = true)
    lateinit var currencyWalletService: CurrencyWalletService
    private lateinit var currencyWalletRepository: CurrencyWalletRepository
    private lateinit var currencyWalletRemoteDataSource: CurrencyWalletRemoteDataSource
    private val mockData = WalletNetworkResponse(
        id = "iurrrrr",
        balance = "600",
        deleted = false,
        name = "Bitcoin Wallet",
        type = "CryptoCoin",
        currency = CurrencyNetworkResponse(
            name = "Bitcoin",
            symbol = "BTC",
            id = "bitcoinid",
            price = 48489.79,
            logo = "https://bitpanda-assets.s3-eu-west-1.amazonaws.com/static/cryptocoin/btc.svg"
        )
    )

    private val mockData1 = WalletNetworkResponse(
        id = "dsfsdfsf",
        balance = "1600",
        deleted = true,
        name = "ETH Wallet",
        type = "CryptoCoin",
        currency = CurrencyNetworkResponse(
            name = "Ethereum",
            symbol = "BTC",
            id = "ethereumid",
            price = 22222.79,
            logo = "https://bitpanda-assets.s3-eu-west-1.amazonaws.com/static/cryptocoin/eth.svg"
        )
    )

    private val mockData2 = WalletNetworkResponse(
        id = "dsfstertedfsf",
        balance = "6000",
        deleted = true,
        name = "Gold Wallet",
        type = "Metal",
        currency = CurrencyNetworkResponse(
            name = "Gold",
            symbol = "GLD",
            id = "goldid",
            price = 34.79,
            logo = "https://bitpanda-assets.s3-eu-west-1.amazonaws.com/static/cryptocoin/eth.svg"
        )
    )

    private val deletedWalletList = ServiceNetworkResponse(
        listOf(
            mockData,
            mockData1,
            mockData2
        )
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        currencyWalletService = mock()
        currencyWalletRemoteDataSource = CurrencyWalletRemoteDataSource(currencyWalletService)
        currencyWalletRepository = CurrencyWalletRepository(currencyWalletRemoteDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun showListOfWalletsWithoutDeletedWallets() {
        runBlockingTest {

            whenever(currencyWalletService.fetchCurrencyWallets())
                .thenReturn(NetworkResponse.Success(deletedWalletList))

            val response = currencyWalletRepository.fetchCurrencyWallets()
            assert(response is RepositoryResult.Success)
            val successResponse = response as RepositoryResult.Success
            successResponse.result?.contains(mockData)
        }
    }

    @Test
    fun getWalletsSuccess() {
        runBlockingTest {
            val walletsNetworkList = ServiceNetworkResponse(
                (listOf(mockData))
            )

            whenever(currencyWalletService.fetchCurrencyWallets())
                .thenReturn(NetworkResponse.Success(walletsNetworkList))
            val response = currencyWalletRepository.fetchCurrencyWallets()
            assertNotNull(response)
            assert(response is RepositoryResult.Success)
        }
    }

    @Test
    fun getWalletsApiError() {
        runBlockingTest {
            whenever(currencyWalletService.fetchCurrencyWallets())
                .thenReturn(NetworkResponse.ApiError("Error", 400))
            val response = currencyWalletRepository.fetchCurrencyWallets()
            assertNotNull(response)
            assert(response is RepositoryResult.Fail<*>)
        }
    }

    @Test
    fun getWalletsNetworkError() {
        runBlockingTest {
            whenever(currencyWalletService.fetchCurrencyWallets())
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            val response = currencyWalletRepository.fetchCurrencyWallets()
            assertNotNull(response)
            assert(response is RepositoryResult.Exception)
        }
    }
}
