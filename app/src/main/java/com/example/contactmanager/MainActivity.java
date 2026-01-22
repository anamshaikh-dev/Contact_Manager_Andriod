package com.example.contactmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewContacts;
    Button btnAddContact;
    EditText etSearch;

    ArrayList<Contact> contactList;
    ArrayList<Contact> filteredList;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Bind Views
        listViewContacts = findViewById(R.id.listViewContacts);
        btnAddContact = findViewById(R.id.btnAddContact);
        etSearch = findViewById(R.id.etName);

        // 🔹 Initialize lists
        contactList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // 🔹 Adapter
        adapter = new ContactAdapter(this, filteredList);
        listViewContacts.setAdapter(adapter);

        // 🔹 Add Contact Button
        btnAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            startActivityForResult(intent, 1);
        });

        // 🔹 Edit Contact (Single Click)
        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            Contact contact = filteredList.get(position);

            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            intent.putExtra("contact", contact);
            intent.putExtra("position", contactList.indexOf(contact));

            startActivityForResult(intent, 2);
        });

        // 🔹 Delete Contact (Long Click with Confirmation)
        listViewContacts.setOnItemLongClickListener((parent, view, position, id) -> {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Contact")
                    .setMessage("Are you sure you want to delete this contact?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Contact contact = filteredList.get(position);
                        contactList.remove(contact);
                        filterContacts(etSearch.getText().toString());
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });

        // 🔹 Search Feature (Step D2)
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    // 🔹 Receive data from Add/Edit Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            Contact contact = (Contact) data.getSerializableExtra("contact");

            if (requestCode == 1) { // Add
                contactList.add(contact);
            } else if (requestCode == 2) { // Edit
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    contactList.set(position, contact);
                }
            }

            filterContacts(etSearch.getText().toString());
        }
    }

    // 🔹 Filter Logic
    private void filterContacts(String text) {
        filteredList.clear();

        if (text.isEmpty()) {
            filteredList.addAll(contactList);
        } else {
            for (Contact c : contactList) {
                if (c.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(c);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}