package com.example.chatapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.CommonRow
import com.example.chatapp.DestinationScreens
import com.example.chatapp.LCViewModel
import com.example.chatapp.TitleText
import com.example.chatapp.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProcessChat
    val context= LocalContext.current
    if (inProgress.value) {
       /* Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {

            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color.Magenta,
                strokeWidth = 5.dp
            )
        }*/
    }
        else {
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabclick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it,context)
            showDialog.value = false
        }
        Scaffold(
            floatingActionButton =
            {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = onFabclick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            }
            ,
            content = {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                    TitleText(txt = "Chats")
                    if(chats.isEmpty()){
                        Column(
                           modifier= Modifier
                               .fillMaxWidth()
                               .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center

                        ) {
                            Text(text = "No Chats Available")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)){
                            this.items(chats){
                                chat->
                                val chatUser =if(chat.user1.userId==userData?.userId){
                                    chat.user2
                                } else {
                                    chat.user1
                                }
                                CommonRow(imageUrl = chatUser.imageUrl, name =chatUser.name ) {
                                    chat.chatId?.let{
                                        navigateTo(navController,DestinationScreens.SingleChat.createRoute(id= it))

                                    }


                                }
                            }

                        }
                    }

                    BottomNavigationMenu(
                        selectedItem = BottomNavigationMenu.CHATLIST,
                        navController = navController
                    )
                }
            }
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatNumber.value = ""
        }, confirmButton = {
            Button(onClick = { onAddChat(addChatNumber.value)
            }) {
                Text(text = "Add Chat")
            }
        }, title = { Text(text = "Add chat") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value, onValueChange = {
                        addChatNumber.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }

        )


    }
    FloatingActionButton(
        onClick = { onFabClick.invoke() },
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }


}