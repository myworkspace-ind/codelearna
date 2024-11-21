const userEId = "[[${userEId}]]";
console.log("User EID:", userEId);

/* 
 * Attention: Must encrypt by this method. 
 * If don't encrypt or encrypted by another way, 
 * The Course cannot send tracking process data to our server
 */
function encode(value) {
    var result = encodeURIComponent(value).replace(/'/g, "%27").replace(/"/g, "%22");
    return result;
}

/**
 * Encode string to Base64
 * @param {string} input - String to encode
 * @returns {string} - Base64 encoded string
 */
function encodeBase64(input) {
    const utf8Bytes = new TextEncoder().encode(input); // Convert to UTF-8
    const base64String = btoa(String.fromCharCode(...utf8Bytes));
    return base64String;
}

function generateBasicAuth() {
    // Data to encode
    const username = userEId;
    console.log("Username:", username);
    const email = "ln2thach@gmail.com";

    // String to encode
    const valueToEncode = `${username}:${email}`;
    console.log("String to encode:", valueToEncode);

    // Base64 encoding
    const encodedValue = encodeBase64(valueToEncode);
    console.log("Base64 encoded value:", encodedValue);

    // Create Basic Authorization header
    const authHeader = `Basic ${encodedValue}`;
    console.log("Authorization Header:", authHeader);

    return authHeader;
}

function openCourse(courseUrl, activityId) {
    const auth2 = generateBasicAuth();
    console.log(auth2);
    console.log("User EID course:", userEId);

    // Minimum parameters to pass to 'index.html'
    var actor = `{"name":["${userEId}"],"mbox":["mailto:ln2thach@gmail.com"],"objectType":"Agent"}`;
    console.log("Actor:", actor);

    var endPoint = `https://mksol.vn/xapi-lrs/${userEId}/`;
    console.log("Endpoint:", endPoint);

    var auth = auth2;

    // Encode parameters
    var params = 'actor=' + encode(actor) + '&endpoint=' + encode(endPoint) + '&auth=' + encode(auth) + '&activity_id=' + encode(activityId);

    var course = `<center><iframe src='${courseUrl}?${params}'></iframe></center>`;
    document.getElementById("course").innerHTML = course;
}

function playCourse() {
    var courseId = $("#sl-course").val();
    var activityId = "";
    var url = "";

    if (courseId == 1) {
        activityId = 'ispring://presentations/D8C37258-3807-4186-BEF6-1F2D6C6CF332';
        url = 'https://myworkspace.vn/access/content/group/c5c14b70-efaa-4d9f-b474-17ed8c795830/Guidelines/DEV/Course_DevAppOnMWS/DevMWS_Lesson06_Explore_JSTree/res/index.html';
    }

    if (courseId == 2) {
        activityId = 'ispring://presentations/F6BE0D12-D111-45D1-9EA6-B7CD9053CCB6';
        url = 'https://myworkspace.vn/access/content/group/c5c14b70-efaa-4d9f-b474-17ed8c795830/Guidelines/DEV/Course_DevAppOnMWS/How_to_upload_and_display_image/res/index.html';
    }

    if (courseId == 3) {
        activityId = 'ispring://presentations/678286AB-F6E3-4732-96BB-B1F1F5B9BB76';
        url = 'https://myworkspace.vn/access/content/group/c5c14b70-efaa-4d9f-b474-17ed8c795830/Guidelines/DEV/Course_DevAppOnMWS/SimpleProject_ImageDetector_v0.1/MWS_ImageDetector_How_to_develop_tool_Image_Detector_Step01/res/index.html';
    }

    openCourse(url, activityId);
}
