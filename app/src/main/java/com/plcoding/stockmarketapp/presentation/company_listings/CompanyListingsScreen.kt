package com.plcoding.stockmarketapp.presentation.company_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.plcoding.stockmarketapp.presentation.destinations.CompanyInfoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompanyListingsEvent.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .semantics {
                    contentDescription = "search_query"
                },
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingsEvent.Refresh)
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("company_list")
            ) {
                items(state.companies.size) { i ->
                    val company = state.companies[i]
                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    CompanyInfoScreenDestination(company.symbol)
                                )
                            }
                            .padding(16.dp)
                            .testTag("company_item_$i")
                    )
                    if (i < state.companies.size) {
                        Divider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
            }

        }
    }
}