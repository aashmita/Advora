package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.advora.model.Report
// ✅ ADD THESES
import com.example.advora.components.admin.AdminDrawer
import com.example.advora.components.admin.AdminTopBar
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen(onNavigate: (String) -> Unit) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, // ✅ IMPORTANT
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black
            )  {
                AdminDrawer(
                    current = "reports",
                    onNavigate = onNavigate
                )
            }
        }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            AdminTopBar(
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )

            // ✅ MAIN CONTENT
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                var selectedTab by remember { mutableStateOf("pending") }

                var reports by remember {
                    mutableStateOf(
                        listOf(
                            Report("iPhone 14 Pro Max 256GB", "John Doe", "Suspicious pricing", "2026-03-22"),
                            Report("Honda City 2020 VX Model", "Jane Smith", "Fake images", "2026-03-21"),
                            Report("Samsung S23 Ultra", "Rahul Sharma", "Duplicate listing", "2026-03-20")
                        )
                    )
                }

                var showDialog by remember { mutableStateOf(false) }
                var selectedReportIndex by remember { mutableStateOf(-1) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {

                    ToggleButton(
                        text = "Pending",
                        selected = selectedTab == "pending"
                    ) {
                        selectedTab = "pending"
                    }

                    ToggleButton(
                        text = "Resolved",
                        selected = selectedTab == "resolved"
                    ) {
                        selectedTab = "resolved"
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                reports.forEachIndexed { index, report ->

                    if (selectedTab == "pending" && !report.isResolved ||
                        selectedTab == "resolved" && report.isResolved
                    ) {

                        ReportCard(
                            title = report.title,
                            user = report.user,
                            reason = report.reason,
                            date = report.date,
                            isResolved = report.isResolved,
                            onResolve = {
                                reports = reports.toMutableList().also {
                                    it[index] = it[index].copy(isResolved = true)
                                }
                            },
                            onDismiss = {
                                selectedReportIndex = index
                                showDialog = true
                            }
                        )
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirm Dismiss") },
                        text = { Text("Are you sure you want to dismiss this report?") },
                        confirmButton = {
                            Button(onClick = {
                                reports = reports.toMutableList().also {
                                    it.removeAt(selectedReportIndex)
                                }
                                showDialog = false
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showDialog = false
                            }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

// 🔘 TOGGLE BUTTON
//
@Composable
fun RowScope.ToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(2.dp)
            .background(
                if (selected) Color(0xFFB96B4C) else Color(0xFF3F3F46),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = Color.White
        )
    }
}

//
// 📋 REPORT CARD
//
@Composable
fun ReportCard(
    title: String,
    user: String,
    reason: String,
    date: String,
    isResolved: Boolean,
    onResolve: () -> Unit,
    onDismiss: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {

        Column(modifier = Modifier.padding(14.dp)) {

            // 🔴 ICON + TITLE
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            if (isResolved) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isResolved) Icons.Default.Check else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isResolved) Color(0xFF22C55E) else Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            Text("Reported by: $user", color = Color.Gray)
            Text("Reason: $reason", color = Color.Gray)
            Text(date, color = Color.Gray)

            Spacer(Modifier.height(12.dp))

            // 🔘 BUTTONS / STATUS
            if (!isResolved) {

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    Button(
                        onClick = onResolve,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF22C55E)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Resolve", color = Color.White)
                    }

                    Button(
                        onClick =onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFEF4444)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dismiss", color = Color.White)
                    }
                }

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFD1FAE5),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Resolved", color = Color(0xFF065F46))
                }
            }
        }
    }
}