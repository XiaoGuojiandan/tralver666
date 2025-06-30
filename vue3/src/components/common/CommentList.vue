<template>
  <div class="comment-list">
    <!-- 评论列表 -->
    <div v-if="comments.length > 0">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <div class="user-info">
            <el-avatar :size="40" :src="comment.user?.avatar || '/src/assets/images/no-image.png'" />
            <div class="user-meta">
              <span class="username">{{ comment.user?.username || '匿名用户' }}</span>
              <span class="time">{{ formatTime(comment.createTime) }}</span>
            </div>
          </div>
          <div class="rating" v-if="comment.rating">
            <el-rate v-model="comment.rating" disabled show-score />
          </div>
        </div>
        <div class="comment-content">
          {{ comment.content }}
        </div>
        <div class="comment-footer">
          <div class="actions">
            <span class="like-action" @click="handleLike(comment)">
              <el-icon :class="{ active: comment.isLiked }"><ThumbsUp /></el-icon>
              {{ comment.likeCount || 0 }}
            </span>
            <span class="reply-action" @click="showReplyInput(comment)">
              <el-icon><ChatLineRound /></el-icon>
              回复
            </span>
          </div>
        </div>
        <!-- 回复列表 -->
        <div v-if="comment.replies && comment.replies.length > 0" class="reply-list">
          <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
            <div class="reply-header">
              <div class="user-info">
                <el-avatar :size="30" :src="reply.user?.avatar || '/src/assets/images/no-image.png'" />
                <div class="user-meta">
                  <span class="username">{{ reply.user?.username || '匿名用户' }}</span>
                  <span class="time">{{ formatTime(reply.createTime) }}</span>
                </div>
              </div>
            </div>
            <div class="reply-content">
              {{ reply.content }}
            </div>
          </div>
        </div>
        <!-- 回复输入框 -->
        <div v-if="comment.showReplyInput" class="reply-input">
          <el-input
            v-model="comment.replyContent"
            type="textarea"
            :rows="2"
            placeholder="写下你的回复..."
          />
          <div class="reply-actions">
            <el-button size="small" @click="hideReplyInput(comment)">取消</el-button>
            <el-button type="primary" size="small" @click="submitReply(comment)">回复</el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- 无评论时显示 -->
    <el-empty v-else description="暂无评论" />
    
    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ThumbsUp, ChatLineRound } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { formatTime } from '@/utils/dateUtils'

const props = defineProps({
  targetId: {
    type: [String, Number],
    required: true
  },
  targetType: {
    type: String,
    required: true
  }
})

const comments = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 加载评论列表
const loadComments = async () => {
  try {
    // 这里需要实现评论列表的API调用
    // const res = await getComments({
    //   targetId: props.targetId,
    //   targetType: props.targetType,
    //   currentPage: currentPage.value,
    //   size: pageSize.value
    // })
    // comments.value = res.data.records
    // total.value = res.data.total

    // 模拟数据
    comments.value = [
      {
        id: 1,
        content: '这是一条测试评论',
        createTime: new Date(),
        rating: 4.5,
        likeCount: 10,
        isLiked: false,
        user: {
          username: '测试用户',
          avatar: null
        },
        replies: []
      }
    ]
    total.value = 1
  } catch (error) {
    ElMessage.error('加载评论失败')
    console.error('加载评论失败:', error)
  }
}

// 处理点赞
const handleLike = async (comment) => {
  try {
    // 这里需要实现点赞的API调用
    comment.isLiked = !comment.isLiked
    comment.likeCount += comment.isLiked ? 1 : -1
    ElMessage.success(comment.isLiked ? '点赞成功' : '取消点赞成功')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('点赞操作失败:', error)
  }
}

// 显示回复输入框
const showReplyInput = (comment) => {
  comment.showReplyInput = true
  comment.replyContent = ''
}

// 隐藏回复输入框
const hideReplyInput = (comment) => {
  comment.showReplyInput = false
  comment.replyContent = ''
}

// 提交回复
const submitReply = async (comment) => {
  if (!comment.replyContent?.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    // 这里需要实现提交回复的API调用
    // const res = await submitReply({
    //   targetId: props.targetId,
    //   targetType: props.targetType,
    //   parentId: comment.id,
    //   content: comment.replyContent
    // })

    // 模拟添加回复
    comment.replies = comment.replies || []
    comment.replies.push({
      id: Date.now(),
      content: comment.replyContent,
      createTime: new Date(),
      user: {
        username: '当前用户',
        avatar: null
      }
    })

    hideReplyInput(comment)
    ElMessage.success('回复成功')
  } catch (error) {
    ElMessage.error('回复失败')
    console.error('提交回复失败:', error)
  }
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  loadComments()
}

// 当前页改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadComments()
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comment-list {
  padding: 20px 0;
}

.comment-item {
  margin-bottom: 20px;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-meta {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 500;
  color: #333;
}

.time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  margin: 10px 0;
  color: #666;
  line-height: 1.6;
}

.comment-footer {
  margin-top: 10px;
}

.actions {
  display: flex;
  gap: 20px;
}

.like-action,
.reply-action {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: #666;
}

.like-action:hover,
.reply-action:hover {
  color: #409eff;
}

.like-action .active {
  color: #409eff;
}

.reply-list {
  margin: 10px 0 0 50px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.reply-item {
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.reply-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.reply-header {
  margin-bottom: 5px;
}

.reply-content {
  margin-left: 40px;
  color: #666;
}

.reply-input {
  margin-top: 10px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 