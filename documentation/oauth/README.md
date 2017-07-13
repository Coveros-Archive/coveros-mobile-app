# Allowing Anonymous Comments Through the Wordpress Backend Files
Since Wordpress 4.7.0., the posting of anonymous comments, or comments that are not associated with a Wordpress account, is natively disabled. To allow anonymous comments, you will need to modify a backend file, which can be accessed by SSHing into the machine hosting the website. 

- In the `functions.php` file, found in the `/var/www/html/wp-content/themes/illdy/functions.php` directory, paste the following code:
```
function filter_rest_allow_anonymous_comments() {
	return true;
}
add_filter('rest_allow_anonymous_comments', 'filter_rest_allow_anonymous_comments');
```

This should allow POST comment requests without the need for authentication.

##### The following instructions are for the Coveros development website found at: https://www3.dev.secureci.com. If this URL changes (i.e. a new development website is created), substitute the above link with the appropriate link in the instructions below.
# Setting Up OAuth2.0 on Wordpress Using the WP OAuth Server Plug-in and Testing With Postman

## Setting up the WP OAuth Server Plug-in
- To set-up the use of OAuth2.0 on Wordpress, install the plug-in [WP OAuth Server](https://wordpress.org/plugins/oauth2-provider/), either through the Wordpress Dashboard or through the console into the `/var/www/html/wp-content/plugins/` directory.


- Once WP OAuth Server plug-in has been installed, navigate to the plug-in by selecting the `OAuth Server` tab in the Wordpress Dashboard. Then, to add a new "Client," or application, select the `Clients` tab and press the `Add New Client` button.


- Fill in the Client name (e.g. "Coveros Mobile App"), the Redirect URI (`https://www.getpostman.com/oauth2/callback` for this example testing with Postman), and a Client description (e.g. "An app for the Mobile Applications Testing course"). Press the `Create Client` button to continue.

At this stage, you will have set up the Wordpress side of using OAuth2.0. For the Client you just created, you should see a "Client ID," as well as a "Client Secret," when you hover under the name of the Client and press the `Show Secret` link. You will use the "Client ID" and "Client Secret" in the next step--testing the OAuth2.0 connection with Postman.

## Testing the OAuth2.0 Connection with Postman

- Open Postman, or [install](https://www.getpostman.com/) the application if you have not already.


- To start a test request, under the `Authorization` tab, which should already be selected, under the dropdown `Type` select `OAuth 2.0`. Press the `Get New Access Token` button that appears.


- Fill out the Token Name (e.g. Coveros Dev Wordpress), the Auth URL (https://www3.dev.secureci.com/oauth/authorize), the Access Token URL (https://www3.dev.secureci.com/oauth/token), the Client ID and Client Secret (from above), and select `Authorization Code` as the Grant Type. Then press the `Request Token` button.

This should bring up a  log-in window, in which you may enter your credentials and approve the OAuth2.0 connection. Note: only users with "Administrator" status will have credentials that will allow for a successful OAuth2.0 connection.

- After logging in, given that valid credentials were used, Postman should now list the new token under "Existing Tokens." Select the token you just created, and then in the "Add token to" dropdown, select the "Header" option. Then, press the "Use Token" button.


Now, let's make an authenticated request to test the OAuth2.0 connection. 

- Select "POST" as the request type, then input `https://www3.dev.secureci.com/wp-json/wp/v2/posts/7509?content=<SOME_NEW_CONTENT_FOR_BLOG_POST>` as the URL. Then, press `Send`. 

If all went well and OAuth2.0 has been set up properly, you should see a JSON that contains the information you provided in the URL above.

> For some reason, while POST requests that edit existing posts and comments work, DELETE requests for posts and comments do not. This is an issue that must be resolved in the future during the "Delete Comments" story.

# Things That Need to Be Addressed in the Future
## Delete Requests

For some reason, while POST requests that edit existing posts and comments work, DELETE requests for posts and comments do not (they return a 403 access error). This is an issue that must be resolved in the future during the "Delete Comments" story. 
Looking at the JSON returned by a GET request at https://www3.dev.secureci.com/wp-json suggests that DELETE requests are possible--in the `routes: {}` entry, `/wp/v2/comments/(?P<id>[\\d]+)` and `/wp/v2/posts/(?P<id>[\\d]+)` both list DELETE under their methods. 

## System user
One glaring issue is that the OAuth2.0 flow involves inputting user credentials; that is, to obtain an access token, a user that has valid authorization (e.g. Administrator), has to log-in with their credentials. While Twitter seems to have support for application-only OAuth authentication] (https://dev.twitter.com/oauth/application-only), Wordpress does not. This poses a problem as we are allowing users to make comments anonymously (without a Wordpress account), but authenticated requests like Editing or Deleting comments (which are later stories) will require a user with a Wordpress account to log-in. One idea to address this problem is to create a system user with proper [authorization that will make REST calls on behalf of the users of the app.

## Redirect URL
This is related to the system user problem. The redirect URL is where the user enters their credentials. In Android, this may be a WebView that will display a log-in screen. If a system user is used, and the application will automatically log-in with its credentials, then the page that brought up by the Redirect URL should never be displayed. Otherwise, if another solution is used for the System user issue discussed above, the redirect URL will need to be considered.

## File permissions
In our attempts at getting OAuth2.0 to work, we changed the file permissions of all the website files so that everyone can read, write, and execute (`chmod 777`). Since we cannot do this to the production site, file permissions may need to be more selectively changed to allow for CRUD requests via OAuth2.0.. 

# What Did Not Work
Initially, we tried using the OAuth that is available natively through Wordpress via the [Jetpack](https://wordpress.org/plugins/jetpack/) plug-in. This involved linking a Wordpress Account within Jetpack's UI within the Wordpress Dashboard, and creating an application (i.e. Client) at https://developer.wordpress.com/apps/ using the linked account. While the OAuth2.0 authentication process seemed to work via Postman (an access token was successfully receieved), requests that required authentication (e.g. POST request for a post) were unsuccessful. 
Two differences between the request for an access token using the WP OAuth Server plug-in and the request for an access token using the natively OAuth available through Wordpress were the Auth URL and the Access Token URL.

|   | Plug-in  | Jetpack  | 
| :---: | :---: | :---: |
| Auth URL  |   https://www3.dev.secureci.com/oauth/authorize   |  	https://public-api.wordpress.com/oauth2/authorize |   
| Access Token URL  | https://www3.dev.secureci.com/oauth/token  | 	https://public-api.wordpress.com/oauth2/token  |   

# Sending an OAuth2.0 Request In Android
There are two articles that helped tremendously in learning how to make OAuth2.0 requests.
- [Auth 2 Simplified](https://aaronparecki.com/oauth-2-simplified/) - lists example requests and their required parameters
- [OAuth2 Authentication] (https://developer.wordpress.com/docs/oauth2/) - more specific to native OAuth2.0 (provided by Wordpress), but another good resource with example requests and instructions on validating tokens
To summarize these articles, there are 3 requests that are made during an OAuth2.0 request.
1. Request for an authorization code

This is a GET request (i.e. load) at the Auth URL with the client id, redirect URL, response code, and scope passed in as parameters. This request will simply take the user to a log-in page where they can log-in and if they have valid credentials to make authenticated requests, their log-in will take what is housing these requests (e.g. a WebView) to the redirect URL, which will now contain the authorization code as a parameter.

2. Request to exchange authorization code for access token

This is a POST request to exchange the authorization code gained from the last request for an access token that, when passed in the header, will allow you to make authenticated requests. This request is made at the Access Token URL, with the client id, redirect URI, client secret, authorization code, and grant type (which should be "authorization_code") passed in as parameters. This POST request should return a JSON that will contain the access token.

3. The actual authenticated request

The access token received in the previous request can now be passed into the header of a request that requires authentication. An example authenticated request is a POST request to posts/<POST_ID>, which will modify the content of the post with the POST_ID.
 


