<?php

define('TELEGRAM_BOT_API_KEY', '1389085993:AAHK-SJ32FGjmExlT3S62Yr3BlBzpf9m2sY');
define('TELEGRAM_CHAT_ID', '-411338339'); // for sending messages to groups use number of chat_id with minus '-' before, example -1238735917

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $payload = json_decode($_POST['payload']);
    $type_event = $_SERVER['HTTP_X_GITHUB_EVENT'];

    $msg = "Event: $type_event\n";
    $msg .= "Repository:  " . $payload->repository->name . "\n";

    switch($type_event) {
        case 'push':
            $msg .= "Commits:\n";
            $commit_id = 1;
            foreach($payload->commits as $commit) {
                $msg .= "- by " . $commit->committer->name . " with message: " . $commit->message . "\n";
                $commit_id++;
            }
            $msg .= "Pushed by: " . $payload->pusher->name . "\n";
            break;
        case 'pull_request':
            $msg .= "Pull request: '" . $payload->pull_request->title . "' was " . $payload->pull_request->state . " by " . $payload->pull_request->user->login . "\n";
            $msg .= "URL: " . $payload->pull_request->html_url . "\n";
            break;
        case 'pull_request_review':
            $msg .= "Pull request: '" . $payload->pull_request->title . "' was " . $payload->review->state . " by " . $payload->review->user->login . "\n";
            $msg .= "URL: " . $payload->pull_request->html_url . "\n";
            break;
        default:
            // nothing
    }

    send_msg($msg);
}
else {
    // do nothing lol :)
}

function send_msg($text)
{

    $text = str_replace("\n", "%0A", $text);
    $method_url = 'https://api.telegram.org/bot' . TELEGRAM_BOT_API_KEY . '/sendMessage';

    $url = $method_url . '?chat_id=' . TELEGRAM_CHAT_ID . '&disable_web_page_preview=1&text=' . $text;

    $response = @file_get_contents($url);
}
