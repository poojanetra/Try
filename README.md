# Try

Here we have functionality to transfer the amount between the accounts for that we have transferamount method.
It takes 3 parameters account deatils of who is transferring, account details of whom to transfer and amount which we want to transfer.
It first checks if account details are not null and account is there and if we have sufficient balance in the account.
After that it updates the amount in both the accounts.
And calls notification service to update both the account holder with message containing account id and amount.
