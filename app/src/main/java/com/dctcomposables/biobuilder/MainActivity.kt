package com.dctcomposables.biobuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dctcomposables.biobuilder.model.PICTURE_LIST
import com.dctcomposables.biobuilder.model.ProfileModel
import com.dctcomposables.biobuilder.ui.theme.BioBuilderTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bioModel: ProfileModel = viewModel()
            var editable by rememberSaveable {
                mutableStateOf(false)
            }
            var changePicture by rememberSaveable() {
                mutableStateOf(false)
            }
            val onChangePicture = {
                changePicture = true
            }
            val onPictureChanged = { pictureId: Int ->
                bioModel.updatePicId(pictureId)
                changePicture = false
            }
            val onClickEdit = {
                editable = true
            }
            var name by rememberSaveable() {
                mutableStateOf(bioModel.name.value)
            }
            val onNameChanged = {changedName: String ->
                name = changedName
            }
            var about by rememberSaveable() {
                mutableStateOf(bioModel.about.value)
            }
            val onAboutChanged = { changedAbout: String ->
                about = changedAbout
            }
            var email by rememberSaveable() {
                mutableStateOf(bioModel.email.value)
            }
            val onEmailChanged = { changedEmail: String ->
                email = changedEmail
            }
            var phone by rememberSaveable() {
                mutableStateOf(bioModel.phone.value)
            }
            val onPhoneChanged = { changedPhone: String ->
                phone = changedPhone
            }

            val onSave = {
                bioModel.updateProfile(
                    name?: "",
                    about?: "",
                    email?:"",
                    phone?:""
                )
                editable = false
            }
            val cancelChooseImage = {
                changePicture = false
            }

            val cancelEdit = {
                editable = false
            }

            val profilePicId = bioModel.profilePicId.observeAsState()
            BioBuilderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AnimatedVisibility(visible = !changePicture) {
                        BioPage(
                            modifier = Modifier.fillMaxWidth(1f),
                            onEdit = onClickEdit,
                            editable = editable,
                            profilePicId = profilePicId.value ?: R.drawable.head,
                            onChangePic = onChangePicture,
                            name = name ?: stringResource(id = R.string.default_name),
                            onNameChanged = onNameChanged,
                            about = about ?: stringResource(id = R.string.default_about),
                            onAboutChanged = onAboutChanged,
                            email = email ?: stringResource(id = R.string.default_email),
                            onEmailChanged = onEmailChanged,
                            phone = phone ?: stringResource(id = R.string.default_phone),
                            onPhoneChanged = onPhoneChanged,
                            onSave = onSave,
                            onEditCancel = cancelEdit
                        )
                    }
                    AnimatedVisibility(visible = changePicture) {
                        ChoosePicture(
                            modifier = Modifier
                                .fillMaxWidth(1f),
                            onChoose = onPictureChanged,
                            onCancel = cancelChooseImage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier, title: String) {
    Surface(
        modifier = modifier,
        elevation = 4.dp
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = title,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun BioTitle(editable: Boolean) {
    if (editable) {
        Title( title = stringResource(id = R.string.edit_profile))
    } else {
        Title(title = stringResource(id = R.string.app_name))
    }
}

@Composable
fun ChoosePicTitle() {
    Title(title = stringResource(id = R.string.choose_image))
}
@Composable
fun BioPage(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    editable: Boolean,
    profilePicId: Int,
    onChangePic: () -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    about: String,
    onAboutChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    onSave: () -> Unit,
    onEditCancel: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BioTitle(editable = editable)
        AnimatedVisibility(visible = !editable) {
            EditButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                onEdit = onEdit
            )
        }
        ProfileImage(
            modifier = modifier
                .padding(32.dp)
                .clip(CircleShape),
            editable = editable,
            picId = profilePicId,
            onChangePic = onChangePic
        )
        val textModifier = Modifier
            .fillMaxWidth(1f)
            .padding(vertical = 16.dp, horizontal = 16.dp)

        EditableName(
            modifier = textModifier,
            editable = editable,
            label = stringResource(id = R.string.name_label),
            text = name,
            onTextChanged = onNameChanged
        )

        EditableAbout(
            modifier = textModifier,
            editable = editable,
            label = stringResource(id = R.string.about_label),
            text = about,
            onTextChanged = onAboutChanged
        )

        EditableText(
            modifier = textModifier,
            editable = editable,
            label = stringResource(id = R.string.email_label),
            text = email,
            onTextChanged = onEmailChanged,
            keyboardType = KeyboardType.Email
        )

        EditableText(
            modifier = textModifier,
            editable = editable,
            label = stringResource(id = R.string.phone_label),
            text = phone,
            onTextChanged = onPhoneChanged,
            keyboardType = KeyboardType.Phone
        )
        AnimatedVisibility(visible = editable) {
            BackHandler {
                onEditCancel()
            }
            SaveButton(onSave = onSave)
        }
    }
}

/*A function for selecting an image from the list*/
@Composable
fun ChoosePicture(
    modifier: Modifier = Modifier,
    onChoose: (Int) -> Unit,
    onCancel: () -> Unit
) {
    BackHandler() {
        onCancel()
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        elevation = 1.dp
    ) {
        Column() {
            ChoosePicTitle()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
            ){
                items(PICTURE_LIST) {picture ->
                    Button(
                        modifier = Modifier.padding(vertical = 4.dp),
                        onClick = {
                            onChoose(picture)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background
                        )
                    ) {
                        Image(
                            modifier = Modifier.size(128.dp),
                            painter = painterResource(id = picture),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    editable: Boolean,
    picId: Int,
    onChangePic: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = picId),
            contentDescription = stringResource(id = R.string.profile_pic)
        )
        AnimatedVisibility(visible = editable) {
            ChangePicButton(onChangePic)
        }
    }
}

@Composable
private fun ChangePicButton(onChangePic: () -> Unit) {
    IconButton(
        onClick = onChangePic
    ) {
        Icon(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.surface.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .padding(horizontal = 20.dp, vertical = 8.dp),
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(id = R.string.update_profile_picture),
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun EditButton(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit
) {
    IconButton( modifier = modifier, onClick = onEdit) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(id = R.string.edit_profile)
        )
    }
}

@Preview
@Composable
fun PreviewEditButon() {
    EditButton {
        
    }
}

@Composable
fun EditableText(
    modifier: Modifier = Modifier,
    editable: Boolean,
    label: String,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    AnimatedVisibility(visible = !editable) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.text_template, label, text),
            style = textStyle
        )
    }
    AnimatedVisibility(visible = editable) {
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = onTextChanged,
            label = {
                Text(text = label)
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
fun EditableName(
    modifier: Modifier = Modifier,
    editable: Boolean,
    label: String,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.h5,
    onTextChanged: (String) -> Unit
) {
    AnimatedVisibility(visible = !editable) {
        Text(
            modifier = modifier,
            text =  text,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
    AnimatedVisibility(visible = editable) {
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = onTextChanged,
            label = {
                Text(text = label)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
    }
}

@Composable
fun EditableAbout(
    modifier: Modifier = Modifier,
    editable: Boolean,
    label: String,
    text: String,
    onTextChanged: (String) -> Unit,
) {
    AnimatedVisibility(visible = !editable) {
        Column(modifier = modifier) {
            Text(
                text = label,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.h6
            )

        }
    }
    AnimatedVisibility(visible = editable) {
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = onTextChanged,
            label = {
                Text(text = label)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
    }
}

@Composable
fun SaveButton(modifier: Modifier = Modifier, onSave: () -> Unit) {
    Button(modifier = modifier, onClick = onSave) {
        Text(text = stringResource(id = R.string.save_label))
    }
}

@Preview
@Composable
fun PreviewProfileImage() {
    BioBuilderTheme() {
        ProfileImage(
            modifier = Modifier
                .padding(32.dp)
                .size(128.dp)
                .clip(CircleShape),
            editable = true,
            picId = R.drawable.head,
            onChangePic = {}
        )
    }
}

@Preview
@Composable
fun PreviewChooseImage() {
    BioBuilderTheme() {
        ChoosePicture(onChoose = {}, onCancel = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBioPage() {
    BioBuilderTheme {
        BioPage(
            onEdit = { /*TODO*/ },
            editable = false,
            profilePicId = R.drawable.head,
            name = "Jake Sully",
            onNameChanged ={} ,
            about = "I am a human-navi hybrid who lives among the other navi people in pandora.",
            onAboutChanged = {},
            email = "jake.sully@pandora.com",
            onEmailChanged = {},
            phone = "111222333444",
            onPhoneChanged = {},
            onChangePic = {},
            onSave = {},
            onEditCancel = {}
        )
    }
}