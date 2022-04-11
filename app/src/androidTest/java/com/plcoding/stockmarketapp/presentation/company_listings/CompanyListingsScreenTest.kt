package com.plcoding.stockmarketapp.presentation.company_listings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.plcoding.stockmarketapp.MainActivity
import com.plcoding.stockmarketapp.di.AppModule
import com.plcoding.stockmarketapp.di.RepositoryModule
import com.plcoding.stockmarketapp.presentation.NavGraphs
import com.plcoding.stockmarketapp.ui.theme.StockMarketAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class CompanyListingsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            StockMarketAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    @Test
    fun test_companies_list_search_query_displayed() {
        composeTestRule.onNodeWithContentDescription("search_query")
            .assertIsDisplayed()
        assert(
            composeTestRule.onNodeWithContentDescription("search_query")
                .currentText()?.isEmpty() ?: false
        )
    }

    @Test
    fun test_companies_list_is_loaded_successfully() {
        composeTestRule.onNodeWithText("b_name", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("company_item_1", useUnmergedTree = true)
            .assert(hasAnyChild(hasText("b_name")))
        composeTestRule.onNodeWithTag("company_item_1")
            .assertTextContains("b_name")

        composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange))
            .assertExists()
            .performTouchInput { swipeUp() }
        composeTestRule.onNodeWithTag("company_item_25")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun test_search_query_works() {
        // Search Query
        composeTestRule.onNodeWithContentDescription("search_query")
            .performTextInput("s_name")
        // Confirm query result
        composeTestRule.onNodeWithTag("company_list")
            .onChildren()
            .assertCountEquals(1)
        composeTestRule.onAllNodes(hasText("s_name"), useUnmergedTree = true)
            .assertCountEquals(2)

        // Reset Query
        composeTestRule.onNodeWithContentDescription("search_query")
            .performTextClearance()
        composeTestRule.onNodeWithContentDescription("search_query")
            .performTextInput("")
        composeTestRule.onNodeWithContentDescription("search_query")
            .performImeAction()

        composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange))
            .assertExists()
            .performTouchInput { swipeUp() }
        composeTestRule.onNodeWithText("a_name", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}

fun SemanticsNodeInteraction.currentText(): String? {
    for ((key, value) in fetchSemanticsNode().config) {
        if (key.name == "EditableText") {
            return value?.toString()
        }
    }
    return null
}