package com.example.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class AddEditContactActivity extends AppCompatActivity {

    EditText etName, etPhone, etEmail;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra("contact");
        if (contact != null) {
            etName.setText(contact.getName());
            etPhone.setText(contact.getPhone());
            etEmail.setText(contact.getEmail());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();

                Contact newContact = new Contact(name, phone, email);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("contact", newContact);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}