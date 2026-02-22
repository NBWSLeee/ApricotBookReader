package cn.corremoon.apricot.bookreader.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import cn.corremoon.apricot.bookreader.AboutActivity
import cn.corremoon.apricot.bookreader.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val languages = listOf(
        R.string.follow_system to "system",
        R.string.simplified_chinese to "zh-CN",
        R.string.english to "en-US"
    )
    var selectedLanguage by remember { mutableStateOf(AppCompatDelegate.getApplicationLocales().toLanguageTags()) }

    @Composable
    fun GetLanguageDisplayName(code: String): String {
        val currentCode = code.ifEmpty { "system" }
        val (titleRes) = languages.find { (_, langCode) ->
            if (langCode == "system") currentCode == "system" else currentCode.startsWith(langCode)
        } ?: (R.string.follow_system to "system")
        return stringResource(id = titleRes)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.nav_settings)) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            item {
                ListItem(
                    headlineContent = { Text(stringResource(id = R.string.language)) },
                    leadingContent = {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = stringResource(id = R.string.language)
                        )
                    },
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = GetLanguageDisplayName(code = selectedLanguage))
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.clickable { showBottomSheet = true }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(id = R.string.about)) },
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.about)
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable { context.startActivity(Intent(context, AboutActivity::class.java)) }
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                @Suppress("AssignedValueIsNeverRead")
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            LazyColumn {
                items(languages.size) { index ->
                    val (titleRes, code) = languages[index]
                    ListItem(
                        headlineContent = { Text(stringResource(id = titleRes)) },
                        trailingContent = {
                            val isSelected = if (code == "system") {
                                selectedLanguage.isEmpty() || selectedLanguage == "system"
                            } else {
                                selectedLanguage.startsWith(code)
                            }
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Selected"
                                )
                            }
                        },
                        modifier = Modifier.clickable {
                            selectedLanguage = code
                            scope.launch {
                                sheetState.hide()
                                @Suppress("AssignedValueIsNeverRead")
                                showBottomSheet = false
                                val localeList = if (code == "system") {
                                    LocaleListCompat.getEmptyLocaleList()
                                } else {
                                    LocaleListCompat.forLanguageTags(code)
                                }
                                AppCompatDelegate.setApplicationLocales(localeList)
                            }
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}
